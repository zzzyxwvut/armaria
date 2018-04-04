package org.example.zzzyxwvut.armaria.security.events;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Objects;

import org.example.zzzyxwvut.armaria.beans.UserBean;
import org.springframework.context.ApplicationEvent;

public final class PrincipalUpdatedEvent extends ApplicationEvent
{
	private static final long serialVersionUID	= -5935207969951592373L;
	private final transient UserBean user;

	private void writeObject(ObjectOutputStream s) throws IOException
	{
		s.defaultWriteObject();
	}

	private void readObject(ObjectInputStream s) throws IOException,
							ClassNotFoundException
	{
		s.defaultReadObject();
	}

	public PrincipalUpdatedEvent(UserBean user)
	{
		super(user);
		this.user	= Objects.requireNonNull(user);
	}

	public UserBean getUser()	{ return user; }
}
