package org.example.zzzyxwvut.armaria.security.beans;

import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;

import org.springframework.security.core.userdetails.UserDetails;

public final class UserDetailsBean implements UserDetails, Serializable
{
	private static final long serialVersionUID	= -3900316947978086729L;
	private final transient PersistentUserBean user;
	private final transient Collection<GrantedAuthorityBean> authorities;

	/* See EJ, #90. */
	private static class UserDetailsBeanProxy implements Serializable
	{
		private static final long serialVersionUID	= 6617150558967538958L;
		private final PersistentUserBean user;
		private final Collection<GrantedAuthorityBean> authorities;

		private UserDetailsBeanProxy(UserDetailsBean user)
		{
			this.user	= Objects.requireNonNull(user.getPersistentUserBean());
			this.authorities = Objects.requireNonNull(user.getAuthorities());
		}

		private Object readResolve()
		{
			return new UserDetailsBean(user, authorities);
		}
	}

	private Object writeReplace()
	{
		return new UserDetailsBeanProxy(this);
	}

	private void readObject(ObjectInputStream stream) throws InvalidObjectException
	{
		throw new InvalidObjectException("UserDetailsBeanProxy required");
	}

	public UserDetailsBean(PersistentUserBean user,
				Collection<GrantedAuthorityBean> authorities)
	{
		this.user	= Objects.requireNonNull(user);
		this.authorities = Objects.requireNonNull(authorities);
	}

	@Override
	public Collection<GrantedAuthorityBean> getAuthorities()
	{
		return authorities;
	}

	private boolean isValidUser()	{ return user.getStatus().isValid(); }

	@Override
	public String getPassword()	{ return user.getPassword(); }

	@Override
	public String getUsername()	{ return user.getLogin(); }

	@Override
	public boolean isAccountNonExpired() { return isValidUser(); }

	@Override
	public boolean isAccountNonLocked() { return isValidUser(); }

	@Override
	public boolean isCredentialsNonExpired() { return isValidUser(); }

	@Override
	public boolean isEnabled()	{ return isValidUser(); }

	public PersistentUserBean getPersistentUserBean() { return user; }
}
