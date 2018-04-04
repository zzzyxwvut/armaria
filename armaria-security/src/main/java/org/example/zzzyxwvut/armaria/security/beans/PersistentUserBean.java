package org.example.zzzyxwvut.armaria.security.beans;

import java.io.Serializable;
import java.util.Objects;

import org.example.zzzyxwvut.armaria.beans.UserBean;
import org.example.zzzyxwvut.armaria.domain.naming.Constants.USERS;

public final class PersistentUserBean implements Serializable
{
	private static final long serialVersionUID	= 1592941274520200350L;
	private final String login;
	private final String password;
	private final String email;
	private final USERS role;
	private final USERS status;

	public PersistentUserBean(UserBean user)
	{
		this.login	= Objects.requireNonNull(user.getLogin());
		this.password	= Objects.requireNonNull(user.getPassword());
		this.email	= Objects.requireNonNull(user.getEmail());
		this.role	= Objects.requireNonNull(user.getRole());
		this.status	= Objects.requireNonNull(user.getStatus());
	}

	public String getLogin()	{ return login; }
	public String getPassword()	{ return password; }
	public String getEmail()	{ return email; }
	public USERS getRole()		{ return role; }
	public USERS getStatus()	{ return status; }
}
