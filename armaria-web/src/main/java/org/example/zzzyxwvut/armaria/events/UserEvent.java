package org.example.zzzyxwvut.armaria.events;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Locale;
import java.util.Objects;

import org.example.zzzyxwvut.armaria.beans.UserBean;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.i18n.LocaleContextHolder;

public class UserEvent extends ApplicationEvent
{
	private static final long serialVersionUID	= -8945028501073797238L;
	protected final transient UserBean user;
	protected final transient Locale locale;

	private void writeObject(ObjectOutputStream s) throws IOException
	{
		s.defaultWriteObject();
	}

	private void readObject(ObjectInputStream s) throws IOException,
							ClassNotFoundException
	{
		s.defaultReadObject();
	}

	public UserEvent(UserBean user, Locale locale)
	{
		super(user);
		this.user	= Objects.requireNonNull(user);
		this.locale	= Objects.requireNonNull(locale);
	}

	public UserEvent(UserBean user, String locale)
	{
		super(user);
		this.user	= Objects.requireNonNull(user);

		if (locale != null) {
			String[] item	= locale.split("_", 3);
			this.locale	= (item.length == 3)
						? new Locale(item[0], item[1], item[2])
						: (item.length == 2)
							? new Locale(item[0], item[1])
							: new Locale(item[0]);
		} else {
			this.locale	= LocaleContextHolder.getLocale();
		}
	}

	public UserBean getUser()	{ return user; }
	public Locale getLocale()	{ return locale; }
}
