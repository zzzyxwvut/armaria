package org.example.zzzyxwvut.armaria.controllers;

import java.security.Principal;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.example.zzzyxwvut.armaria.beans.UserBean;
import org.example.zzzyxwvut.armaria.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

@Controller
@PreAuthorize("isAuthenticated()")
@RequestMapping(produces = MediaType.TEXT_HTML_VALUE)
public class ProfileController
{
	@Autowired
	private UserService userService;

	@Autowired
	private CookieLocaleResolver localeResolver;

	private static final String defaultLocale	= Locale.US.toString();

	@GetMapping("/profile")
	public String profile(HttpServletRequest request, HttpServletResponse response,
				Principal principal, Model model,
				@CookieValue(value = "locale", required = false)
							String currentLocale)
	{
		UserBean user	= userService.getUserByLogin(principal.getName());
		String storedLocale	= user.getLocale().toString();

		/* Restore the preferred locale. */
		if (currentLocale == null && !defaultLocale.equals(storedLocale))
			localeResolver.setLocale(request, response,
							user.getLocale());

		model.addAttribute("qualified",
			user.getLoans().size() > 0 || user.getTickets().size() > 0);
		return "profile";
	}
}
