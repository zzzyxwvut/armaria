package org.example.zzzyxwvut.armaria.security.beans;

import java.io.Serializable;
import java.util.Objects;

import org.example.zzzyxwvut.armaria.domain.naming.Constants.USERS;
import org.springframework.security.core.GrantedAuthority;

public final class GrantedAuthorityBean implements GrantedAuthority, Serializable
{
	private static final long serialVersionUID = -4692721104933914925L;

	/**
	 * @serial	an authority that is granted to the principal
	 */
	private final String role;

	public GrantedAuthorityBean(USERS role)
	{
		this.role	= Objects.requireNonNull(role.toString());
	}

	@Override
	public String getAuthority()	{ return role; }

	@Override
	public String toString()	{ return role; }
}
