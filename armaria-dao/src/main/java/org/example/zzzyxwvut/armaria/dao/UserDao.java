package org.example.zzzyxwvut.armaria.dao;

import java.time.LocalDateTime;

import org.example.zzzyxwvut.armaria.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserDao extends PagingAndSortingRepository<User, Long>
{
	/*
	 * Query methods should have a {find,read,get,query,stream} prefix;
	 * delete methods should have a {delete,remove} prefix;
	 * also {count} and {exists} prefixes are supported.
	 *
	 * See org.springframework.data.repository.query.parser.PartTree
	 */

	Long deleteByTesseraTokenNotNullAndTesseraEntryLessThanEqual(LocalDateTime yesterday);

	@EntityGraph(type = EntityGraphType.LOAD, attributePaths = { "loans", "tickets" })
	User getByEmail(String email);

	@EntityGraph(type = EntityGraphType.LOAD, attributePaths = { "loans", "tickets" })
	User getByLogin(String login);

	@Override
	@EntityGraph(type = EntityGraphType.LOAD, attributePaths = { "loans", "tickets" })
	User findOne(Long userId);

	@Override
	@EntityGraph(type = EntityGraphType.LOAD, attributePaths = { "loans", "tickets" })
	Page<User> findAll(Pageable pageable);
}
