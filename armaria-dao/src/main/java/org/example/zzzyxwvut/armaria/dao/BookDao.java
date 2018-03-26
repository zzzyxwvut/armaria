package org.example.zzzyxwvut.armaria.dao;

import org.example.zzzyxwvut.armaria.domain.Book;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface BookDao extends PagingAndSortingRepository<Book, Integer>
{

}
