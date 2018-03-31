package org.example.zzzyxwvut.armaria.service.impl;

import org.example.zzzyxwvut.armaria.beans.BookBean;
import org.example.zzzyxwvut.armaria.dao.BookDao;
import org.example.zzzyxwvut.armaria.domain.Book;
import org.example.zzzyxwvut.armaria.dozer.EntityBeanConverter;
import org.example.zzzyxwvut.armaria.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BookServiceImpl implements BookService
{
	@Autowired
	private BookDao bookDao;

	@Autowired
	private EntityBeanConverter<Book, BookBean> converter;

	@SuppressWarnings("unchecked")
	@Override
	public Page<BookBean> getItemsOnPage(Pageable pageable)
	{
		return bookDao.findAll(pageable).map(new Converter<Book, BookBean>() {
			@Override
			public BookBean convert(Book book)
			{
				return converter.toBean(book, BookBean.class);
			}
		});
	}

	@Override
	public BookBean getBookById(Integer bookId)
	{
		return converter.toBean(bookDao.findOne(bookId), BookBean.class);
	}

	@Override
	public long count()	{ return bookDao.count(); }

	@Override
	public void saveBook(BookBean book)
	{
		bookDao.save(converter.toEntity(book, Book.class));
	}

	@Override
	public void deleteBook(Integer bookId)
	{
		throw new UnsupportedOperationException();
	}
}
