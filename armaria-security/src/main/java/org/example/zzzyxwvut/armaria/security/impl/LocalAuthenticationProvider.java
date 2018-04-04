package org.example.zzzyxwvut.armaria.security.impl;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.zzzyxwvut.armaria.beans.UserBean;
import org.example.zzzyxwvut.armaria.crypto.Encryptor;
import org.example.zzzyxwvut.armaria.security.beans.GrantedAuthorityBean;
import org.example.zzzyxwvut.armaria.security.beans.PersistentUserBean;
import org.example.zzzyxwvut.armaria.security.beans.UserDetailsBean;
import org.example.zzzyxwvut.armaria.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public final class LocalAuthenticationProvider implements AuthenticationProvider
{
	@Autowired
	private UserService userService;

	private final Logger logger	= LogManager.getLogger();

	@Override
	public Authentication authenticate(Authentication auth)
						throws AuthenticationException
	{
		/* Strive to follow a time-constant algorithm. */
		final String fakeWord	= Encryptor.INSTANCE.encrypt("");
		final String formWord	= auth.getCredentials().toString();
		UserBean user	= userService.getUserByLogin(auth.getName());
		boolean found	= (user != null);

		/* Do not reveal whether the user or the password is invalid. */
		if (!Encryptor.INSTANCE.equals(
				Encryptor.INSTANCE.decrypt(((found)
					? user.getPassword()
					: fakeWord.toString())), formWord) || !found)
			throw new BadCredentialsException(new StringBuilder(64)
				.append("False credentials: ")
				.append(auth.getName()).toString());

		Collection<GrantedAuthorityBean> authorities	= new ArrayList<>();
		authorities.add(new GrantedAuthorityBean(user.getRole()));
		UserDetailsBean client	= new UserDetailsBean(
				new PersistentUserBean(user), authorities);

		if (!client.isAccountNonLocked())
			throw new LockedException(new StringBuilder(64)
				.append("The ").append(client.getUsername())
				.append(" account is locked.").toString());

		logger.debug(new StringBuilder(64)
			.append("Authenticated as ").append(client.getUsername())
			.append(" : ").append(client.getPersistentUserBean().getRole())
			.toString());
		return new UsernamePasswordAuthenticationToken(client,
				client.getPassword(), client.getAuthorities());
	}

	@Override
	public boolean supports(Class<?> clazz)
	{
		return UsernamePasswordAuthenticationToken.class.equals(clazz);
	}
}
