package org.example.zzzyxwvut.armaria.events;

import org.example.zzzyxwvut.armaria.beans.UserBean;

public final class LocaleChangedEvent extends UserEvent
{
	private static final long serialVersionUID	= -2539937986523355747L;

	public LocaleChangedEvent(UserBean user, String locale)
	{
		super(user, locale);
	}
}
