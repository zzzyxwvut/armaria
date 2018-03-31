package org.example.zzzyxwvut.armaria.service;

import org.example.zzzyxwvut.armaria.beans.BookBean;

public interface BookService extends GenericService
{
	BookBean getBookById(Integer bookId);

	long count();

	void saveBook(BookBean book);

	void deleteBook(Integer bookId);
}
