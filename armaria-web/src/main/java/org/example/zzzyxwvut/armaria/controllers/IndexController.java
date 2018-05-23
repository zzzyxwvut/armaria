package org.example.zzzyxwvut.armaria.controllers;

import java.security.Principal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.zzzyxwvut.armaria.beans.UserBean;
import org.example.zzzyxwvut.armaria.events.LocaleChangedEvent;
import org.example.zzzyxwvut.armaria.events.PasswordRecalledEvent;
import org.example.zzzyxwvut.armaria.events.VerificationEvent;
import org.example.zzzyxwvut.armaria.service.UserService;
import org.example.zzzyxwvut.armaria.validators.ForgottenPasswordValidator;
import org.example.zzzyxwvut.armaria.validators.NewUserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(produces = MediaType.TEXT_HTML_VALUE)
public class IndexController
{
	@Autowired
	private UserService userService;

	@Autowired
	private NewUserValidator newUserValidator;

	@Autowired
	private ForgottenPasswordValidator forgottenPasswordValidator;

	@Autowired
	private ApplicationEventPublisher publisher;

	private final Logger logger	= LogManager.getLogger();

	@InitBinder("newUser")
	protected void initNewBinder(WebDataBinder binder)
	{
		binder.setValidator(newUserValidator);
	}

	@InitBinder("forgetfulUser")
	protected void initForgottenBinder(WebDataBinder binder)
	{
		binder.setValidator(forgottenPasswordValidator);
	}

	@GetMapping({"/", "/index", "/main", "/signin"})
	public String index()	{ return "index"; }

	@PostMapping("/translate")
	public String translate(@RequestParam(value = "locale",
					defaultValue = "en_US") String locale,
							Principal principal)
	{
		if (principal != null) {
			UserBean user = userService.getUserByLogin(principal.getName());
			publisher.publishEvent(new LocaleChangedEvent(user, locale));
		}

		return "redirect:/index";
	}

	@GetMapping("/forgotten")
	public String forgotten(Model model)
	{
		model.addAttribute("forgotten", Boolean.TRUE);
		return "index";
	}

	@PostMapping("/remember")
	public String remember(@Validated @ModelAttribute("forgetfulUser") UserBean forgetfulUser,
				BindingResult result, RedirectAttributes attr,
				@ModelAttribute("localeCookie") String locale)
	{
		if (result.hasErrors()) {
			logger.debug(result);
			attr.addFlashAttribute("org.springframework.validation.BindingResult.forgetfulUser",
								result);
			attr.addFlashAttribute("forgetfulUser",	forgetfulUser);
			attr.addFlashAttribute("forgotten",	Boolean.TRUE);
			return "redirect:/index";
		}

		attr.addFlashAttribute("salute", forgetfulUser);
		UserBean realUser	= userService.getUserByLogin(forgetfulUser.getLogin());
		publisher.publishEvent(new PasswordRecalledEvent(realUser, locale));
		return "redirect:/index";
	}

	@PostMapping("/signup")
	public String signup(@Validated @ModelAttribute("newUser") UserBean newUser,
				BindingResult result, RedirectAttributes attr,
				@RequestParam("copyWord") String password,
				@ModelAttribute("localeCookie") String locale)
	{
		if (password == null || !newUser.getPassword().equals(password))
			result.rejectValue("password", "password.other");

		if (result.hasErrors()) {
			logger.debug(result);
			attr.addFlashAttribute("org.springframework.validation.BindingResult.newUser",
								result);
			attr.addFlashAttribute("newUser", newUser);
			attr.addFlashAttribute("copyWord", password);
			return "redirect:/index";
		}

		logger.debug("Registered a new user " + newUser.getLogin());
		attr.addFlashAttribute("salute", newUser);
		publisher.publishEvent(new VerificationEvent(newUser, locale));
		return "redirect:/index";
	}

	@ModelAttribute("newUser")
	public UserBean reachNewUser()		{ return new UserBean(); }

	@ModelAttribute("oldUser")
	public UserBean fetchOldUser()		{ return new UserBean(); }

	@ModelAttribute("forgetfulUser")
	public UserBean fetchForgetfulUser()	{ return new UserBean(); }

	@ModelAttribute("localeCookie")
	public String getLocale(@CookieValue(value = "locale", defaultValue = "en_US")
								String locale)
	{
		return locale;
	}
}
