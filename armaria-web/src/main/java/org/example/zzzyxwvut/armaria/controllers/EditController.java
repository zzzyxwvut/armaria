package org.example.zzzyxwvut.armaria.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.zzzyxwvut.armaria.annotation.AuthenticatedPersistentUserBean;
import org.example.zzzyxwvut.armaria.beans.BookBean;
import org.example.zzzyxwvut.armaria.beans.LoanBean;
import org.example.zzzyxwvut.armaria.beans.TicketBean;
import org.example.zzzyxwvut.armaria.beans.UserBean;
import org.example.zzzyxwvut.armaria.crypto.Encryptor;
import org.example.zzzyxwvut.armaria.domain.naming.Constants.BOOKS;
import org.example.zzzyxwvut.armaria.events.MaturedTicketEvent;
import org.example.zzzyxwvut.armaria.security.beans.PersistentUserBean;
import org.example.zzzyxwvut.armaria.security.events.PrincipalUpdatedEvent;
import org.example.zzzyxwvut.armaria.service.BookService;
import org.example.zzzyxwvut.armaria.service.TicketService;
import org.example.zzzyxwvut.armaria.service.UserService;
import org.example.zzzyxwvut.armaria.validators.OldUserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@PreAuthorize("isAuthenticated()")
@RequestMapping(produces = MediaType.TEXT_HTML_VALUE)
public class EditController
{
	@Autowired
	private BookService bookService;

	@Autowired
	private TicketService ticketService;

	@Autowired
	private UserService userService;

	@Autowired
	private OldUserValidator oldUserValidator;

	@Autowired
	private ApplicationEventPublisher publisher;

	private final Logger logger	= LogManager.getLogger();

	@InitBinder("dummyUser")
	protected void initBinder(WebDataBinder binder)
	{
		binder.setValidator(oldUserValidator);
	}

	@GetMapping("/edit")
	public String edit()	{ return "edit"; }

	@PreAuthorize("!hasAuthority('patron')")
	@PostMapping("/leave")
	public String leave(@AuthenticatedPersistentUserBean PersistentUserBean user0)
	{
		if (user0.getRole().isPatron())
			return "redirect:/edit";

		UserBean user	= userService.getUserByLogin(user0.getLogin());
		Iterable<TicketBean> tickets	= ticketService.getAllTickets();

		for (LoanBean loan : user.getLoans()) {
			BookBean book	= loan.getBook();
			boolean found	= false;

			for (TicketBean ticket : tickets) {
				if (!ticket.getBook().equals(book))
					continue;

				publisher.publishEvent(new MaturedTicketEvent(
					loan.getId(), ticket, null, "en_US"));
				found	= true;
				break;
			}

			if (!found) {
				book.setStatus(BOOKS.AVAILABLE);
				bookService.saveBook(book);
			}
		}

		userService.deleteUser(user.getId());
		user.setPassword("");
		logger.debug("Removed a user " + user);
		return "redirect:/logout";	/* NOTE: The default Security URL. */
	}

	@PostMapping("/update")
	public String update(@Validated @ModelAttribute("dummyUser") UserBean dummyUser,
				BindingResult result, RedirectAttributes attr,
				@AuthenticatedPersistentUserBean PersistentUserBean user,
				@RequestParam("copyWord") String password)
	{
		if (password == null || !dummyUser.getPassword().equals(password))
			result.rejectValue("password", "password.other");

		if (result.hasErrors()) {
			logger.debug(result);
			attr.addFlashAttribute("org.springframework.validation.BindingResult.dummyUser",
								result);
			attr.addFlashAttribute("dummyUser", dummyUser);
			attr.addFlashAttribute("copyWord", password);
			return "redirect:/edit";
		}

		/* Ignore the cached UserDetailsBean instance. */
		UserBean currentUser	= userService.getUserByLogin(user.getLogin());
		boolean changed		= false;

		if (dummyUser.getPassword() != null
				&& dummyUser.getPassword().length() > 0) {
			currentUser.setPassword(
				Encryptor.INSTANCE.encrypt(dummyUser.getPassword()));
			changed	= true;
		}

		if (dummyUser.getEmail() != null
				&& dummyUser.getEmail().length() > 0
				&& !dummyUser.getEmail().equals(user.getEmail())) {
			currentUser.setEmail(dummyUser.getEmail());
			changed	= true;
		}

		if (!changed)
			return "redirect:/edit";

		userService.saveUser(currentUser);
		publisher.publishEvent(new PrincipalUpdatedEvent(currentUser));
		return "redirect:/profile";
	}

	@ModelAttribute("dummyUser")
	public UserBean copyOldUser(@AuthenticatedPersistentUserBean PersistentUserBean user)
	{
		UserBean dummy	= new UserBean();
		dummy.setLogin(user.getLogin());
		dummy.setPassword("");
		dummy.setEmail(user.getEmail());
		return dummy;
	}
}
