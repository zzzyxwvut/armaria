package org.example.zzzyxwvut.armaria.events;

import org.example.zzzyxwvut.armaria.beans.UserBean;

public final class PasswordRecalledEvent extends UserEvent
{
	private static final long serialVersionUID	= 5512539377216250279L;

	public PasswordRecalledEvent(UserBean user, String locale)
	{
		super(user, locale);
	}
}
