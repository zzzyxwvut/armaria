package org.example.zzzyxwvut.armaria.listeners;

import org.example.zzzyxwvut.armaria.beans.UserBean;
import org.example.zzzyxwvut.armaria.events.LocaleChangedEvent;
import org.example.zzzyxwvut.armaria.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public final class LocaleChangedListener
{
	@Autowired
	private UserService userService;

	@EventListener
	public void handleLocaleChangedEvent(LocaleChangedEvent event)
	{
		UserBean user	= event.getUser();
		user.setLocale(event.getLocale());
		userService.saveUser(user);
	}
}
