package org.example.zzzyxwvut.armaria.dozer.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.dozer.Mapper;
import org.example.zzzyxwvut.armaria.beans.BookBean;
import org.example.zzzyxwvut.armaria.beans.LoanBean;
import org.example.zzzyxwvut.armaria.beans.TicketBean;
import org.example.zzzyxwvut.armaria.beans.UserBean;
import org.example.zzzyxwvut.armaria.domain.Book;
import org.example.zzzyxwvut.armaria.domain.Loan;
import org.example.zzzyxwvut.armaria.domain.Ticket;
import org.example.zzzyxwvut.armaria.domain.User;
import org.example.zzzyxwvut.armaria.dozer.EntityBeanConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EntityBeanConverterImpl<D, B> implements EntityBeanConverter<D, B>
{
	@Resource
	private Mapper bookMapper;

	@Resource
	private Mapper loanMapper;

	@Resource
	private Mapper ticketMapper;

	@Resource
	private Mapper userMapper;

	private Mapper fetchMapper(Class<?> clazz)
	{
		assert clazz != null;

		if (BookBean.class.isAssignableFrom(clazz) ||
				Book.class.isAssignableFrom(clazz)) {
			return bookMapper;
		} else if (LoanBean.class.isAssignableFrom(clazz) ||
				Loan.class.isAssignableFrom(clazz)) {
			return loanMapper;
		} else if (TicketBean.class.isAssignableFrom(clazz) ||
				Ticket.class.isAssignableFrom(clazz)) {
			return ticketMapper;
		} else if (UserBean.class.isAssignableFrom(clazz) ||
				User.class.isAssignableFrom(clazz)) {
			return userMapper;
		} else {
			throw new IllegalArgumentException();
		}
	}

	@Override
	public B toBean(D entity, Class<B> beanClass)
	{
		if (entity == null)
			return null;

		return fetchMapper(beanClass).map(entity, beanClass);
	}

	@Override
	public List<B> toBeanList(Iterable<D> entities, Class<B> beanClass)
	{
		if (entities == null)
			return null;

		List<B> beans	= new ArrayList<>();
		Mapper mapper	= fetchMapper(beanClass);

		for (D entity : entities)
			beans.add(mapper.map(entity, beanClass));

		return beans;
	}

	@Override
	public D toEntity(B bean, Class<D> entityClass)
	{
		if (bean == null)
			return null;

		return fetchMapper(entityClass).map(bean, entityClass);
	}
}
