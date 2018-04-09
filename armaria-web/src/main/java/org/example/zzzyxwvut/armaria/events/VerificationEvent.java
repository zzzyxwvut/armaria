package org.example.zzzyxwvut.armaria.events;

import org.example.zzzyxwvut.armaria.beans.UserBean;

public final class VerificationEvent extends UserEvent
{
	private static final long serialVersionUID	= -8896740514074520664L;

	public VerificationEvent(UserBean user, String locale)
	{
		super(user, locale);
	}
}
