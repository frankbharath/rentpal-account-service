package com.rentpal.accounts.service.interfaces;

import com.rentpal.accounts.constants.Constants.Tokentype;
import com.rentpal.accounts.model.Mail;

import javax.mail.MessagingException;

/**
 * The Interface EmailService.
 *
 * @author bharath
 * @version 1.0
 * Creation time: Jul 12, 2020 10:54:44 PM
 */

public interface EmailService {
	
	/**
	 * Send email.
	 *
	 * @param mail the mail
	 * @throws MessagingException the messaging exception
	 */
	public void sendEmail(Mail mail) throws MessagingException;
	
	/**
	 * Send token email to user.
	 *
	 * @param userid the userid
	 * @param email the email
	 * @param token the token
	 * @param type the type
	 * @throws MessagingException the messaging exception
	 */
	public void sendTokenEmailToUser(Long userid, String email, String token, Tokentype type) throws MessagingException;
}
