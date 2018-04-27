package org.example.zzzyxwvut.armaria.listeners;

import java.util.UUID;

import org.example.zzzyxwvut.armaria.beans.UserBean;
import org.example.zzzyxwvut.armaria.crypto.Encryptor;
import org.example.zzzyxwvut.armaria.events.PasswordRecalledEvent;
import org.example.zzzyxwvut.armaria.mail.Writer;
import org.example.zzzyxwvut.armaria.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public final class PasswordRecalledListener
{
	@Autowired
	private UserService userService;

	@Autowired
	private Writer writer;

	@EventListener
	public void handlePasswordRecalledEvent(PasswordRecalledEvent event)
	{
		UserBean user	= event.getUser();
		String password	= String.format("%x", UUID.randomUUID()
						.getMostSignificantBits());
		user.setPassword(Encryptor.INSTANCE.encrypt(password));

		userService.saveUser(user);
		writer.composeAndSend(event.getLocale(), user.getEmail(),
				"msg.recall.subject", null,
				"msg.recall.body", new Object[] { password });
	}
}
