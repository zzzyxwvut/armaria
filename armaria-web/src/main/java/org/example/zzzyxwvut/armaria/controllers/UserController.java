package org.example.zzzyxwvut.armaria.controllers;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.zzzyxwvut.armaria.beans.BookBean;
import org.example.zzzyxwvut.armaria.beans.LoanBean;
import org.example.zzzyxwvut.armaria.beans.TicketBean;
import org.example.zzzyxwvut.armaria.beans.UserBean;
import org.example.zzzyxwvut.armaria.domain.naming.Constants.BOOKS;
import org.example.zzzyxwvut.armaria.domain.naming.Constants.USERS;
import org.example.zzzyxwvut.armaria.events.MaturedTicketEvent;
import org.example.zzzyxwvut.armaria.paging.PageUtil;
import org.example.zzzyxwvut.armaria.service.BookService;
import org.example.zzzyxwvut.armaria.service.TicketService;
import org.example.zzzyxwvut.armaria.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@PreAuthorize("hasAuthority('patron')")
@RequestMapping(value = "/users", produces = MediaType.TEXT_HTML_VALUE)
public class UserController
{
	@Autowired
	private BookService bookService;

	@Autowired
	private TicketService ticketService;

	@Autowired
	private UserService userService;

	@Autowired
	private ApplicationEventPublisher publisher;

	private final Logger logger	= LogManager.getLogger();

	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(HttpStatus.FOUND)
	public ModelAndView handle(HttpServletRequest request, Exception e)
	{
		return new ModelAndView("redirect:/users/1");
	}	/* Whenever the only user on page is removed. */

	@PostMapping("/patronize")
	public String patronize(@RequestParam("patron") Long userId,
						RedirectAttributes attr)
	{
		attr.addFlashAttribute("patron", userId);
		return "redirect:/loans/items";
	}

	@PostMapping("/toggle")
	public String toggle(@RequestParam("user") Long userId,
				@ModelAttribute("viewPage") Integer page)
	{
		UserBean user	= userService.getUserById(userId);

		if (user.getRole().isPatron())
			return "redirect:/users/" + page;

		user.setStatus(user.getStatus().isValid()
					? USERS.INVALID
					: USERS.VALID);
		userService.saveUser(user);
		return "redirect:/users/" + page;
	}

	@PostMapping("/expunge")
	public String expunge(@RequestParam("user") Long userId,
				@ModelAttribute("viewPage") Integer page)
	{
		UserBean user	= userService.getUserById(userId);

		if (user.getRole().isPatron()) {
			return "redirect:/users/" + page;
		} else if (user.getStatus().isValid()) {
			user.setStatus(USERS.INVALID);
			userService.saveUser(user);
			return "redirect:/users/" + page;
		}	/* Remove invalid users only. */

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

		userService.deleteUser(userId);
		user.setPassword("");
		logger.debug("Removed a user " + user);
		return "redirect:/users/" + page;
	}

	@GetMapping("/{page:[1-9]+[\\d]*}")
	public ModelAndView walk(@PathVariable("page") Integer page,
			@ModelAttribute("orderBy") String[] order, Model model)
	{
		model.addAttribute("viewPage",	page);
		return PageUtil.renderPaginated(userService, "users", model,
				page, 10, 10, Sort.Direction.ASC, order);
	}

	@ModelAttribute("orderBy")
	public String[] order(@RequestParam(value = "orderBy",
			defaultValue = "id") String[] order)
	{
		return order;
	}
}
