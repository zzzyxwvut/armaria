package org.example.zzzyxwvut.armaria.controllers;

import java.security.Principal;

import org.example.zzzyxwvut.armaria.beans.BookBean;
import org.example.zzzyxwvut.armaria.beans.LoanBean;
import org.example.zzzyxwvut.armaria.beans.TicketBean;
import org.example.zzzyxwvut.armaria.beans.UserBean;
import org.example.zzzyxwvut.armaria.domain.naming.Constants.BOOKS;
import org.example.zzzyxwvut.armaria.events.MaturedTicketEvent;
import org.example.zzzyxwvut.armaria.service.BookService;
import org.example.zzzyxwvut.armaria.service.LoanService;
import org.example.zzzyxwvut.armaria.service.TicketService;
import org.example.zzzyxwvut.armaria.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@PreAuthorize("isAuthenticated()")
@SessionAttributes("patron")
@RequestMapping(value = "/loans", produces = MediaType.TEXT_HTML_VALUE)
public class LoanController
{
	@Autowired
	private BookService bookService;

	@Autowired
	private LoanService loanService;

	@Autowired
	private TicketService ticketService;

	@Autowired
	private UserService userService;

	@Autowired
	private ApplicationEventPublisher publisher;

	@GetMapping("/items")
	public String items(@ModelAttribute("user") UserBean user)
	{
		if (user.getRole().isPatron()
					|| user.getLoans().size() > 0
					|| user.getTickets().size() > 0)
			return "loans";

		return "redirect:/profile";
	}

	@PostMapping("/restore")
	public String restore(@RequestParam("book") Integer bookId,
				@RequestParam("loan") Long loanId)
	{
		BookBean book	= bookService.getBookById(bookId);

		if (!book.getStatus().isBorrowed())
			return "redirect:/loans/items";

		for (TicketBean ticket : ticketService.getAllTickets()) {
			if (!ticket.getBook().equals(book))
				continue;

			publisher.publishEvent(new MaturedTicketEvent(
						loanId, ticket, null, "en_US"));
			return "redirect:/loans/items";
		}

		book.setStatus(BOOKS.AVAILABLE);

		bookService.saveBook(book);
		loanService.deleteLoan(loanId);
		return "redirect:/loans/items";
	}

	@PostMapping("/cancel")
	public String cancel(@RequestParam("ticket") Long ticketId)
	{
		ticketService.deleteTicket(ticketId);
		return "redirect:/loans/items";
	}

	@ModelAttribute("deputy")
	public UserBean deputy(Principal principal)
	{
		return userService.getUserByLogin(principal.getName());
	}

	@PreAuthorize("hasAuthority('patron')")
	@ModelAttribute("user")		/* The patron flashes "patron". */
	public UserBean patron(@ModelAttribute("patron") Long userId, Model model)
	{
		model.addAttribute("patron", userId);
		return userService.getUserById(userId);
	}

	@ModelAttribute("user")
	public UserBean user(@ModelAttribute("deputy") UserBean deputy, Model model)
	{
		if (model.containsAttribute("user"))
			return (UserBean) model.asMap().get("user");

		return deputy;
	}

	@ModelAttribute("loans")
	public Iterable<LoanBean> getAllLoans(@ModelAttribute("user") UserBean user)
	{
		return user.getLoans();
	}

	@ModelAttribute("tickets")
	public Iterable<TicketBean> getAllTickets(@ModelAttribute("user") UserBean user)
	{
		return user.getTickets();
	}

	@ModelAttribute("principal")
	public String principal(@ModelAttribute("deputy") UserBean deputy,
					@ModelAttribute("user") UserBean user)
	{
		if (deputy.getRole().isPatron())
			return (new StringBuilder(32)).append(deputy.getLogin())
				.append(" \"as\" ").append(user.getLogin()).toString();

		return user.getLogin();
	}
}
