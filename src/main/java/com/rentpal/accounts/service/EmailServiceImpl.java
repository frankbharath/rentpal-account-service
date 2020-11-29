package com.rentpal.accounts.service;

import com.rentpal.accounts.constants.Constants.Tokentype;
import com.rentpal.accounts.model.Mail;
import com.rentpal.accounts.service.interfaces.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * The Class EmailServiceImpl.
 *
 * @author bharath
 * @version 1.0
 * Creation time: Jul 12, 2020 1:49:10 AM
 */

@Component
public class EmailServiceImpl implements EmailService {
	
	/** The Constant VERIFICAIONTEMPLATE. */
	public static final String VERIFICAIONTEMPLATE="mail/verification";
	
	/** The Constant RESTPASSWORDTEMPLATE. */
	public static final String RESTPASSWORDTEMPLATE="mail/resetpassword";
	
	/** The Constant VERIFICAIONAPI. */
	public static final String VERIFICAIONAPI="/verify";
	
	/** The Constant RESETPASSWORDAPI. */
	public static final String RESETPASSWORDAPI="/reset";
	
	/** The java mail sender. */
	private final JavaMailSender javaMailSender;

    /** The template engine. */
    private final SpringTemplateEngine templateEngine;

	/** The Environment variable to read application properties.*/
	private final Environment env;

	private MessageSource messageSource;
	
	/**
	 * Instantiates a new email service impl.
	 *
	 * @param javaMailSender the java mail sender
	 * @param templateEngine the template engine
	 */
	@Autowired
	public EmailServiceImpl(JavaMailSender javaMailSender, SpringTemplateEngine templateEngine, Environment env) {
		this.javaMailSender=javaMailSender;
		this.templateEngine=templateEngine;
		this.env=env;
	}

	@Autowired
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	/**
	 * Send email.
	 *
	 * @param mail the mail
	 * @throws MessagingException the messaging exception
	 */
	public void sendEmail(final Mail mail) throws MessagingException {
		MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());
	        
        helper.setTo(mail.getTo());
        if(mail.getTemplate()!=null) {
        	Context context = new Context();
        	if(mail.getModel()!=null) {
                context.setVariables(mail.getModel());
            }
        	String html = templateEngine.process(mail.getTemplate(), context);
        	helper.setText(html, true);
        }
        helper.setSubject(mail.getSubject());
        helper.setFrom(mail.getFrom());
        javaMailSender.send(message);
    }
	
	/**
	 * Send token email to user.
	 *
	 * @param userid the userid
	 * @param email the email
	 * @param token the token
	 * @param type the type
	 * @throws MessagingException the messaging exception
	 */
	public void sendTokenEmailToUser(final Long userid, final String email, final String token, final Tokentype type) throws MessagingException {
		Map<String,Object> model=new HashMap<>();
		model.put("user", email.split("@")[0]);
		String url=new StringBuilder(Boolean.parseBoolean(env.getProperty("server.ssl.enabled"))?"https://":"http://")
				.append(env.getProperty("server.address"))
				.append((type==Tokentype.VERIFY?VERIFICAIONAPI:RESETPASSWORDAPI))
				.append("?token=").append(token).toString();
		model.put("url", url);
		switch(type) {
			case RESET:
				String message=messageSource.getMessage("email.resetpassword.subject", null, LocaleContextHolder.getLocale());
				Mail resetemail=new Mail(getSupportEmail(), email, message);
				resetemail.setTemplate(RESTPASSWORDTEMPLATE);
				resetemail.setModel(model);
				sendEmail(resetemail);
			break;
			case VERIFY:
				message=messageSource.getMessage("email.verification.subject", null, LocaleContextHolder.getLocale());
				Mail verifymail=new Mail(getSupportEmail(),email, message);
				verifymail.setTemplate(VERIFICAIONTEMPLATE);
				verifymail.setModel(model);
				sendEmail(verifymail);
			break;
		}
	}
	
	/**
	 * Gets the support email.
	 *
	 * @return the support email
	 */
	public String getSupportEmail() {
		return env.getProperty("spring.mail.username");
	}
	
}
