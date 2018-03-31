package org.example.zzzyxwvut.armaria.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GenericService
{
	<T> Page<T> getItemsOnPage(Pageable pageable);
}
