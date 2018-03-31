package org.example.zzzyxwvut.armaria.service;

import org.example.zzzyxwvut.armaria.beans.TicketBean;

public interface TicketService extends GenericService
{
	Iterable<TicketBean> getAllTickets();

	long count();

	void saveTicket(TicketBean ticket);

	void deleteTicket(Long ticketId);
}
