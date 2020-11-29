package com.rentpal.accounts.service;


import com.rentpal.accounts.model.SpringUserDetails;
import com.rentpal.accounts.model.User;
import com.rentpal.accounts.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * The Class SpringUserDetailsService.
 *
 * @author bharath
 * @version 1.0
 * Creation time: Jun 29, 2020 7:50:41 PM
 */

@Service
@Qualifier("userDetailService")
public class SpringUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

	private final MessageSource messageSource;
	
	/**
	 * Instantiates a new spring user details service.
	 *
	 * @param userRepository the user DAO impl
	 */
	@Autowired
	public SpringUserDetailsService(UserRepository userRepository, MessageSource messageSource) {
		this.userRepository=userRepository;
		this.messageSource=messageSource;
	}

	/**
	 * Load user by username.
	 *
	 * @param email the email
	 * @return the user details
	 * @throws UsernameNotFoundException the username not found exception
	 */
	@Override
	public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
		User user=userRepository.findByEmail(email).orElse(null);
		if(user!=null) {
			SpringUserDetails userDetails = new SpringUserDetails(user.getEmail(), user.getPassword());
			userDetails.setEnabled(user.isVerified());
			userDetails.setUserId(user.getId());
			return userDetails;
		}
		throw new UsernameNotFoundException(messageSource.getMessage("error.user.no_account", null, LocaleContextHolder.getLocale()));
	}
}
