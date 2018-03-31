package org.example.zzzyxwvut.armaria.task.impl;

import java.time.LocalDateTime;

import org.example.zzzyxwvut.armaria.dao.UserDao;
import org.example.zzzyxwvut.armaria.task.ExpiredRegistrationEntryTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ExpiredRegistrationEntryTaskImpl implements ExpiredRegistrationEntryTask
{
	@Autowired
	private UserDao userDao;

	@Override
	public void purge()
	{
		LocalDateTime yesterday	= LocalDateTime.now().minusHours(24L);
		userDao.deleteByTesseraTokenNotNullAndTesseraEntryLessThanEqual(yesterday);
	}
}
