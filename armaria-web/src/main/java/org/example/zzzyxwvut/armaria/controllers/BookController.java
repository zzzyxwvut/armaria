package org.example.zzzyxwvut.armaria.controllers;

import java.security.Principal;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.example.zzzyxwvut.armaria.beans.BookBean;
import org.example.zzzyxwvut.armaria.beans.LoanBean;
import org.example.zzzyxwvut.armaria.beans.TicketBean;
import org.example.zzzyxwvut.armaria.beans.UserBean;
import org.example.zzzyxwvut.armaria.domain.naming.Constants.BOOKS;
import org.example.zzzyxwvut.armaria.domain.naming.Constants.TICKETS;
import org.example.zzzyxwvut.armaria.handlers.DefaultExceptionModelAndView;
import org.example.zzzyxwvut.armaria.paging.PageUtil;
import org.example.zzzyxwvut.armaria.security.beans.UserDetailsBean;
import org.example.zzzyxwvut.armaria.service.BookService;
import org.example.zzzyxwvut.armaria.service.LoanService;
import org.example.zzzyxwvut.armaria.service.TicketService;
import org.example.zzzyxwvut.armaria.service.UserService;
import org.example.zzzyxwvut.armaria.validators.BookQuotaValidator;
import org.example.zzzyxwvut.armaria.validators.TicketQuotaValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@SessionAttributes("orderBy")
@RequestMapping(value = "/books", produces = MediaType.TEXT_HTML_VALUE)
public class BookController
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
	private BookQuotaValidator bookQuotaValidator;

	@Autowired
	private TicketQuotaValidator ticketQuotaValidator;

	@InitBinder("ticketQuotaUser")
	protected void initTicketBinder(WebDataBinder binder)
	{
		binder.setValidator(ticketQuotaValidator);
		binder.setDisallowedFields("*");
	}

	@InitBinder("bookQuotaUser")
	protected void initBookBinder(WebDataBinder binder)
	{
		binder.setValidator(bookQuotaValidator);
		binder.setDisallowedFields("*");
	}

	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ModelAndView handle(HttpServletRequest request, Exception e)
	{
		return DefaultExceptionModelAndView.populate(request,
			HttpStatus.NOT_FOUND, new ModelAndView("invalid"), e);
	}

	@PreAuthorize("hasAuthority('patron')")
	@PostMapping("/toggle")
	public String toggle(@RequestParam("book") Integer id,
		@ModelAttribute("viewPage") Integer page, Authentication auth)
	{
		UserDetailsBean user = (UserDetailsBean) auth.getPrincipal();

		if (!user.getPersistentUserBean().getRole().isPatron())
			return "redirect:/books/" + page;

		BookBean book	= bookService.getBookById(id);
		book.setStatus(book.getStatus().isBorrowed()
					? BOOKS.AVAILABLE
					: BOOKS.BORROWED);
		bookService.saveBook(book);
		return "redirect:/books/" + page;
	}

	@PreAuthorize("isAuthenticated()")
	@PostMapping("/borrow")
	public String borrow(@Validated @ModelAttribute("bookQuotaUser") UserBean user,
				BindingResult result, RedirectAttributes attr,
				@RequestParam("book") Integer id,
				@ModelAttribute("viewPage") Integer page)
	{
		attr.addFlashAttribute("bookId", id);

		if (result.hasErrors()) {
			attr.addFlashAttribute("org.springframework.validation.BindingResult.bookQuotaUser",
								result);
			attr.addFlashAttribute("bookQuotaUser", user);
			return "redirect:/books/" + page;
		}

		BookBean book	= bookService.getBookById(id);

		if (book.getStatus().isBorrowed())
			return "redirect:/books/" + page;

		book.setStatus(BOOKS.BORROWED);
		LoanBean loan	= new LoanBean();
		loan.setBook(book);
		loan.setUser(user);

		loanService.saveLoan(loan);
		bookService.saveBook(book);
		return "redirect:/loans/items";
	}

	@PreAuthorize("isAuthenticated()")
	@PostMapping("/waitlist")
	public String waitlist(@Validated @ModelAttribute("ticketQuotaUser") UserBean user,
				BindingResult result, RedirectAttributes attr,
				@RequestParam("book") Integer id,
				@ModelAttribute("viewPage") Integer page)
	{
		attr.addFlashAttribute("bookId", id);

		if (result.hasErrors()) {
			attr.addFlashAttribute("org.springframework.validation.BindingResult.ticketQuotaUser",
								result);
			attr.addFlashAttribute("ticketQuotaUser", user);
			return "redirect:/books/" + page;
		}

		BookBean book	= bookService.getBookById(id);

		if (!book.getStatus().isBorrowed())
			return "redirect:/books/" + page;

		TicketBean ticket	= new TicketBean();
		ticket.setBookId(book.getId());	/* business key's part */
		ticket.setUserId(user.getId());	/* business key's part */
		ticket.setBook(book);
		ticket.setUser(user);

		LoanBean loan	= new LoanBean();
		loan.setBookId(book.getId());	/* business key's part */
		loan.setUserId(user.getId());	/* business key's part */

		Set<TicketBean> tickets	= user.getTickets();
		Set<LoanBean> loans	= user.getLoans();

		/*
		 * Verify that the book is neither borrowed nor put
		 * on the waiting list already by the same user.
		 */
		if (tickets.contains(ticket) || loans.contains(loan)) {
			result.rejectValue("tickets", "quota.duplicate");
			attr.addFlashAttribute("org.springframework.validation.BindingResult.ticketQuotaUser",
								result);
			attr.addFlashAttribute("ticketQuotaUser", user);
			return "redirect:/books/" + page;
		}

		ticket.setStatus(TICKETS.VALID);
		ticketService.saveTicket(ticket);
		return "redirect:/loans/items";
	}

	@GetMapping("/sort")
	public ModelAndView sort(@RequestParam(value = "orderBy",
			defaultValue = "id") String[] order, Model model)
	{
		/* Persist order via @SessionAttributes. */
		model.addAttribute("orderBy", order);
		model.addAttribute("viewPage",	1);
		return PageUtil.renderPaginated(bookService, "books", model,
				1, 45, 10, Sort.Direction.ASC, order);
	}

	@GetMapping("/{page:[1-9]+[\\d]*}")
	public ModelAndView walk(@PathVariable("page") Integer page,
			@ModelAttribute("orderBy") String[] order, Model model)
	{
		model.addAttribute("viewPage",	page);
		return PageUtil.renderPaginated(bookService, "books", model,
				page, 45, 10, Sort.Direction.ASC, order);
	}

	@ModelAttribute("orderBy")
	public String[] order(@RequestParam(value = "orderBy",
			defaultValue = "id") String[] order)
	{
		return order;
	}

	@ModelAttribute("bookQuotaUser")
	public UserBean fetchQuotaUser(@ModelAttribute("user") UserBean user)
	{
		return user;
	}

	@ModelAttribute("ticketQuotaUser")
	public UserBean fetchTicketUser(@ModelAttribute("user") UserBean user)
	{
		return user;
	}

	@ModelAttribute("user")
	public UserBean fetchUser(Principal principal)
	{
		return (principal == null)
			? new UserBean()
			: userService.getUserByLogin(principal.getName());
	}
}
