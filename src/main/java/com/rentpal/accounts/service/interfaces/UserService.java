package com.rentpal.accounts.service.interfaces;

import com.rentpal.accounts.dto.UserDTO;
import com.rentpal.accounts.model.User;

import javax.mail.MessagingException;

public interface UserService {
	
	UserDTO addUser(User user, String confirmPassword) throws MessagingException;
	
	void verifyAccountForUser(String token);
	
	void sendResetPasswordLink(String email) throws MessagingException;

	String getUserEmailForToken(String id);

	void resetPassword(String token, String password, String confirmPassword);
	
	void changePassword(String password, String confirmPassword);
}
