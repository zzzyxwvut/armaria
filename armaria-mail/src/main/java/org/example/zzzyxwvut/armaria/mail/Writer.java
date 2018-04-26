package org.example.zzzyxwvut.armaria.mail;

import java.util.Locale;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

public class Writer
{
	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private Postmaster postmaster;

	private final Logger logger	= LogManager.getLogger();

	public Writer() { }

	private MimeMessage compose(Locale locale, String recipient,
					String subject, Object[] args0,
					String message, Object[] args1)
							throws MessagingException
	{
		String title	= messageSource.getMessage(subject, args0, locale);
		String body	= messageSource.getMessage(message, args1, locale);
		String head	= messageSource.getMessage("msg.head", null, locale);
		String tail	= messageSource.getMessage("msg.tail", null, locale);
		MimeMessage email	= mailSender.createMimeMessage();
		MimeMessageHelper help	= new MimeMessageHelper(email, true);
		help.setTo(recipient);
		help.setSubject(title);
		help.setText(new StringBuilder(128).append(head)
			.append(body).append(tail).toString(), true);
		ClassPathResource resource =
			new ClassPathResource("images/doric_fluted-0.png");
		help.addInline("doricFluted", resource);
		return email;
	}

	public void composeAndSend(Locale locale, String recipient,
					String subject, Object[] args0,
					String message, Object[] args1)
	{
		try {
			postmaster.send(compose(locale, recipient,
					subject, args0, message, args1), 2);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
}
