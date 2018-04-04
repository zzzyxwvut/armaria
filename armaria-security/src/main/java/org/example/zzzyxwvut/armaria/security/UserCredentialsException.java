package org.example.zzzyxwvut.armaria.security;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class UserCredentialsException extends AuthenticationException
{
	private static final long serialVersionUID	= -479203386670017124L;

	public UserCredentialsException() { super("Authentication failed"); }
	public UserCredentialsException(String msg) { super(msg); }
	public UserCredentialsException(String msg, Throwable t) { super(msg, t); }
}
