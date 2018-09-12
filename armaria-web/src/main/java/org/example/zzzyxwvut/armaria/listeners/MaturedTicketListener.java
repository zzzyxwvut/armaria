package org.example.zzzyxwvut.armaria.listeners;

import org.example.zzzyxwvut.armaria.beans.LoanBean;
import org.example.zzzyxwvut.armaria.beans.TicketBean;
import org.example.zzzyxwvut.armaria.events.MaturedTicketEvent;
import org.example.zzzyxwvut.armaria.mail.Writer;
import org.example.zzzyxwvut.armaria.service.LoanService;
import org.example.zzzyxwvut.armaria.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public final class MaturedTicketListener
{
	@Autowired
	private LoanService loanService;

	@Autowired
	private TicketService ticketService;

	@Autowired
	private Writer writer;

	@EventListener
	public void handleMaturedTicketEvent(MaturedTicketEvent event)
	{
		Long loanId		= event.getLoanId();
		TicketBean ticket	= event.getTicket();
		String title		= ticket.getBook().getTitle();

		LoanBean anotherLoan	= new LoanBean();
		anotherLoan.setBook(ticket.getBook());
		anotherLoan.setUser(ticket.getUser());

		loanService.deleteLoan(loanId);
		ticketService.deleteTicket(ticket.getId());
		loanService.saveLoan(anotherLoan);

		writer.composeAndSend(event.getLocale(), ticket.getUser().getEmail(),
				"msg.mature.subject", null,
				"msg.mature.body", new Object[] { title });
	}
}
