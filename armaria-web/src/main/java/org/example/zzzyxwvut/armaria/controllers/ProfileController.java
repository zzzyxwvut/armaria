package org.example.zzzyxwvut.armaria.controllers;

import java.security.Principal;

import org.example.zzzyxwvut.armaria.beans.UserBean;
import org.example.zzzyxwvut.armaria.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@PreAuthorize("isAuthenticated()")
@RequestMapping(produces = MediaType.TEXT_HTML_VALUE)
public class ProfileController
{
	@Autowired
	private UserService userService;

	@GetMapping("/profile")
	public String profile(Principal principal, Model model)
	{
		UserBean user	= userService.getUserByLogin(principal.getName());
		model.addAttribute("qualified",
			user.getLoans().size() > 0 || user.getTickets().size() > 0);
		return "profile";
	}
}
