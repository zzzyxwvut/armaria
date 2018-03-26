package org.example.zzzyxwvut.armaria.dao;

import org.example.zzzyxwvut.armaria.domain.Ticket;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface TicketDao extends PagingAndSortingRepository<Ticket, Long>
{
	@Override
	@EntityGraph(type = EntityGraphType.LOAD, attributePaths = { "user.loans", "user.tickets" })
	Iterable<Ticket> findAll();
}
