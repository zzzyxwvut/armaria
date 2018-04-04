package org.example.zzzyxwvut.armaria.security.listeners;

import java.util.ArrayList;
import java.util.Collection;

import org.example.zzzyxwvut.armaria.beans.UserBean;
import org.example.zzzyxwvut.armaria.security.beans.GrantedAuthorityBean;
import org.example.zzzyxwvut.armaria.security.beans.PersistentUserBean;
import org.example.zzzyxwvut.armaria.security.beans.UserDetailsBean;
import org.example.zzzyxwvut.armaria.security.events.PrincipalUpdatedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public final class PrincipalUpdatedListener
{
	@EventListener
	public void handlePrincipalUpdatedEvent(PrincipalUpdatedEvent event)
	{
		UserBean currentUser	= event.getUser();

		/* Re-authenticate the current user. */
		Collection<GrantedAuthorityBean> authorities	= new ArrayList<>();
		authorities.add(new GrantedAuthorityBean(currentUser.getRole()));
		Authentication auth	= new UsernamePasswordAuthenticationToken(
			new UserDetailsBean(new PersistentUserBean(currentUser),
								authorities),
			currentUser.getPassword(), authorities);

		if (auth.isAuthenticated())
			SecurityContextHolder.getContext().setAuthentication(auth);
	}
}
