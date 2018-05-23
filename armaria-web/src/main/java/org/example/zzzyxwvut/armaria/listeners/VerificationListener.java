package org.example.zzzyxwvut.armaria.listeners;

import java.time.LocalDateTime;
import java.util.UUID;

import org.example.zzzyxwvut.armaria.beans.UserBean;
import org.example.zzzyxwvut.armaria.domain.naming.Constants.USERS;
import org.example.zzzyxwvut.armaria.events.VerificationEvent;
import org.example.zzzyxwvut.armaria.mail.Writer;
import org.example.zzzyxwvut.armaria.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public final class VerificationListener
{
	@Autowired
	private UserService userService;

	@Autowired
	private Writer writer;

	@EventListener
	public void handleVerificationEvent(VerificationEvent event)
	{
		UserBean user	= event.getUser();
		UUID uuid	= UUID.randomUUID();
		String token	= String.format("%x%x",
						uuid.getMostSignificantBits(),
						uuid.getLeastSignificantBits());
		user.getTessera().setEntry(LocalDateTime.now());
		user.getTessera().setToken(token);
		user.setLocale(event.getLocale());

		if (userService.count() == 0L)
			user.setRole(USERS.PATRON);

		userService.saveNewUser(user);
		writer.composeAndSend(event.getLocale(), user.getEmail(),
				"msg.verify.subject", new Object[] { token },
				"msg.verify.body", null);
	}
}
