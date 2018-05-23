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
		this.locale	= (locale != null)
					? locale
					: LocaleContextHolder.getLocale();
		this.user.setLocale(this.locale);
	}

	public UserEvent(UserBean user, String locale)
	{
		super(user);
		this.user	= Objects.requireNonNull(user);

		if (locale != null) {
			String[] items	= locale.split("_", 3);
			this.locale	= (items.length == 3)
					? new Locale(items[0], items[1], items[2])
					: (items.length == 2)
						? new Locale(items[0], items[1])
						: new Locale(items[0]);
		} else {
			this.locale	= LocaleContextHolder.getLocale();
		}

		this.user.setLocale(this.locale);
	}

	public UserBean getUser()	{ return user; }
	public Locale getLocale()	{ return locale; }
}
