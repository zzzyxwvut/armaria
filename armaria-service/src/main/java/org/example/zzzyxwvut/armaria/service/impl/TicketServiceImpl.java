package org.example.zzzyxwvut.armaria.service.impl;

import org.example.zzzyxwvut.armaria.beans.TicketBean;
import org.example.zzzyxwvut.armaria.dao.TicketDao;
import org.example.zzzyxwvut.armaria.domain.Ticket;
import org.example.zzzyxwvut.armaria.dozer.EntityBeanConverter;
import org.example.zzzyxwvut.armaria.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TicketServiceImpl implements TicketService
{
	@Autowired
	private TicketDao ticketDao;

	@Autowired
	private EntityBeanConverter<Ticket, TicketBean> converter;

	@SuppressWarnings("unchecked")
	@Override
	public Page<TicketBean> getItemsOnPage(Pageable pageable)
	{
		return ticketDao.findAll(pageable).map(new Converter<Ticket, TicketBean>() {
			@Override
			public TicketBean convert(Ticket ticket)
			{
				return converter.toBean(ticket, TicketBean.class);
			}
		});
	}

	@Override
	public Iterable<TicketBean> getAllTickets()
	{
		return converter.toBeanList(ticketDao.findAll(), TicketBean.class);
	}

	@Override
	public long count()	{ return ticketDao.count(); }

	@Override
	public void saveTicket(TicketBean ticket)
	{
		ticketDao.save(converter.toEntity(ticket, Ticket.class));
	}

	@Override
	public void deleteTicket(Long ticketId) { ticketDao.delete(ticketId); }
}
