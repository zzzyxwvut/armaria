package org.example.zzzyxwvut.armaria.events;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Locale;
import java.util.Objects;

import org.example.zzzyxwvut.armaria.beans.TicketBean;

public final class MaturedTicketEvent extends UserEvent
{
	private static final long serialVersionUID	= 1135454208353345887L;
	private final transient Long loanId;
	private final transient TicketBean ticket;

	private void writeObject(ObjectOutputStream s) throws IOException
	{
		s.defaultWriteObject();
	}

	private void readObject(ObjectInputStream s) throws IOException,
							ClassNotFoundException
	{
		s.defaultReadObject();
	}

	public MaturedTicketEvent(Long loanId, TicketBean ticket, String locale)
	{
		super(ticket.getUser(), locale);
		this.loanId	= Objects.requireNonNull(loanId);
		this.ticket	= Objects.requireNonNull(ticket);
	}

	public MaturedTicketEvent(Long loanId, TicketBean ticket, Locale locale)
	{
		super(ticket.getUser(), locale);
		this.loanId	= Objects.requireNonNull(loanId);
		this.ticket	= Objects.requireNonNull(ticket);
	}

	public Long getLoanId()		{ return loanId; }
	public TicketBean getTicket()	{ return ticket; }
}
