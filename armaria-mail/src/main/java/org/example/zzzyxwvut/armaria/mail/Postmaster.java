package org.example.zzzyxwvut.armaria.mail;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import javax.mail.Address;
import javax.mail.Flags.Flag;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.zzzyxwvut.armaria.beans.UserBean;
import org.example.zzzyxwvut.armaria.domain.naming.Constants.USERS;
import org.example.zzzyxwvut.armaria.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;

public class Postmaster
{
	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private UserService userService;

	private final Logger logger	= LogManager.getLogger();

	@Async("taskmaster")
	public Future<Void> send(MimeMessage message, int attempts)
	{
		long delay	= (ThreadLocalRandom.current().nextLong() & 3L)
									+ 1L;

		do {
			try {
				TimeUnit.SECONDS.sleep(delay);
				mailSender.send(message);
				break;
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				delay	= (ThreadLocalRandom.current().nextLong()
								& 15L) + 8L;
			}	/* Re-send the message in 8-23 seconds. */
		} while (--attempts > 0);

		return new AsyncResult<>(null);
	}

	@Async("taskmaster")
	public Future<Void> readRepliedMessage(MimeMessage message)
	{
		Future<Void> result	= new AsyncResult<>(null);

		try {
			Address[] from	= message.getFrom();

			if (!(from instanceof InternetAddress[]) || from.length < 1) {
				logger.error("Requires the RFC 822 \"From\" header field");
				return result;
			}

			String email	= ((InternetAddress[]) from)[0].getAddress();
			UserBean user	= userService.getUserByEmail(email);

			if (user == null)
				return result;

			String userToken	= user.getTessera().getToken();
			String mailToken	= message.getSubject();
			LocalDateTime userEntry	= user.getTessera().getEntry();
			LocalDateTime mailEntry	= (message.getSentDate() != null)
				? LocalDateTime.ofInstant(
					message.getSentDate().toInstant(),
					ZoneId.systemDefault())
				: LocalDateTime.now();

			if (userToken == null || mailToken == null
					|| userEntry == null || mailEntry == null
					|| !mailToken.matches(".*" + userToken + ".*")
					|| mailEntry.isAfter(userEntry.plusHours(24L)))
				return result;

			user.getTessera().setToken(null);
			user.setStatus(USERS.VALID);
			userService.saveUser(user);
			message.setFlag(Flag.FLAGGED, true);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return result;
	}
}
