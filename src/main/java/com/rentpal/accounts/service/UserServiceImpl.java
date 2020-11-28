package com.rentpal.accounts.service;


import com.rentpal.accounts.common.DTOModelMapper;
import com.rentpal.accounts.common.Utils;
import com.rentpal.accounts.constants.Constants;
import com.rentpal.accounts.constants.Constants.Tokentype;
import com.rentpal.accounts.dto.UserDTO;
import com.rentpal.accounts.exception.APIRequestException;
import com.rentpal.accounts.model.Token;
import com.rentpal.accounts.model.TokenId;
import com.rentpal.accounts.model.User;
import com.rentpal.accounts.repository.TokenRepository;
import com.rentpal.accounts.repository.UserRepository;

import com.rentpal.accounts.service.interfaces.EmailService;
import com.rentpal.accounts.service.interfaces.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;

/**
 * The type User service.
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class UserServiceImpl implements UserService {

	private final PasswordEncoder passwordEncoder;
	
	private final EmailService emailService;

	private final UserRepository userRepository;

	private final TokenRepository tokenRepository;

	/**
	 * Instantiates a new User service.
	 *
	 * @param userRepository  the user repository
	 * @param tokenRepository the token repository
	 * @param passwordEncoder the password encoder
	 * @param emailUtil       the email util
	 */
	@Autowired
	public UserServiceImpl(UserRepository userRepository, TokenRepository tokenRepository, PasswordEncoder passwordEncoder, EmailService emailUtil) {
		this.userRepository=userRepository;
		this.tokenRepository=tokenRepository;
		this.passwordEncoder = passwordEncoder;
		this.emailService = emailUtil;
	}

	@Override
	public UserDTO addUser(User user, String confirmPassword) throws MessagingException{
		user.setEmail(user.getEmail().toLowerCase());
		//checks if the password are equal
		if(!user.getPassword().equals(confirmPassword)) {
			throw new APIRequestException("error.register.password.mismatch");
		}
		//checks if email already exists in the database
		if(userRepository.existsByEmail(user.getEmail())) {
			throw new APIRequestException("error.user.email.exists");
		}
		user.setCreationTime(System.currentTimeMillis());
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		//save the user to the database
		User newUser=userRepository.save(user);
		sendVerificationLinkToUser(newUser);
		log.info("Added new user to database");
		return DTOModelMapper.userModelDTOMapper(user);
	}

	private void sendVerificationLinkToUser(User user) throws MessagingException {
		Token token=new Token();
		TokenId tokenId=new TokenId(user, Tokentype.VERIFY.getValue());
		token.setTokenId(tokenId);
		token.setCreationTime(System.currentTimeMillis());
		token.setId(Utils.generateAlphaNumericString(Constants.ALPHANUMLEN));
		tokenRepository.save(token);
		emailService.sendTokenEmailToUser(user.getId(), user.getEmail(), token.getId(), Tokentype.VERIFY);
		log.info("Verification link sent for user");
	}

	@Override
	public void verifyAccountForUser(String id) {
		Token token=tokenRepository.findById(id).orElse(null);
		//check if token exists else if token has expired else verify user
		if(token==null){
			throw new APIRequestException("error.user.verification");
		}else if(token.getCreationTime()+Constants.EXPIRATIONINTERVAL<System.currentTimeMillis()){
			tokenRepository.delete(token);
			userRepository.deleteById(token.getTokenId().getUser().getId());
			throw new APIRequestException("error.user.verification");
		}else{
			tokenRepository.delete(token);
			userRepository.verifyUser(token.getTokenId().getUser().getId());
			log.info("User account verified");
		}
	}

	@Override
	public void sendResetPasswordLink(String email) throws MessagingException {
		User user=userRepository.findByEmail(email).orElse(null);
		// check if email exists or else fail silently
		if(user!=null && user.isVerified()) {
			Token token=new Token();
			TokenId tokenId=new TokenId(user, Tokentype.RESET.getValue());
			token.setTokenId(tokenId);
			token.setCreationTime(System.currentTimeMillis());
			token.setId(Utils.generateAlphaNumericString(Constants.ALPHANUMLEN));
			TokenId delTokenId=new TokenId(user, Tokentype.RESET.getValue());
			tokenRepository.deleteByTokenId(delTokenId);
			tokenRepository.save(token);
			emailService.sendTokenEmailToUser(user.getId(), email, token.getId(), Tokentype.RESET);
			log.info("Password reset link sent for the user");
		}
	}

	@Override
	public String getUserEmailForToken(String id){
		Token token=tokenRepository.findById(id).orElse(null);
		if(token==null){
			throw new APIRequestException("error.user.reset");
		}else if(token.getCreationTime()+Constants.EXPIRATIONINTERVAL<System.currentTimeMillis()){
			tokenRepository.delete(token);
			throw new APIRequestException("error.user.reset");
		}
		return token.getTokenId().getUser().getEmail();
	}

	@Override
	public void resetPassword(String id, String password, String confirmPassword) {
		//check if password are equal
		if(!password.equals(confirmPassword)) {
			throw new APIRequestException("error.register.password.mismatch");
		}
		Token token=tokenRepository.findById(id).orElse(null);
		//check if token exists else if token has expired else update password
		if(token==null){
			throw new APIRequestException("error.user.reset");
		}else if(token.getCreationTime()+Constants.EXPIRATIONINTERVAL<System.currentTimeMillis()){
			tokenRepository.delete(token);
			throw new APIRequestException("error.user.reset");
		}else{
			tokenRepository.delete(token);
			userRepository.updatePassword(token.getTokenId().getUser().getId(), passwordEncoder.encode(password));
			log.info("User resets the password");
		}
	}
	
	@Override
	public void changePassword(String password, String confirmPassword) {
		//check if password are equal
		if(!password.equals(confirmPassword)) {
			throw new APIRequestException("error.register.password.mismatch");
		}
		userRepository.updatePassword(Utils.getUserId(), passwordEncoder.encode(password));
		log.info("User changed the password");
	}
}
