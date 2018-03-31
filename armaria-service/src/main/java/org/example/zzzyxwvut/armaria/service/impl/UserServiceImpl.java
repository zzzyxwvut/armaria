package org.example.zzzyxwvut.armaria.service.impl;

import org.example.zzzyxwvut.armaria.beans.UserBean;
import org.example.zzzyxwvut.armaria.crypto.Encryptor;
import org.example.zzzyxwvut.armaria.dao.UserDao;
import org.example.zzzyxwvut.armaria.domain.User;
import org.example.zzzyxwvut.armaria.dozer.EntityBeanConverter;
import org.example.zzzyxwvut.armaria.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService
{
	@Autowired
	private UserDao userDao;

	@Autowired
	private EntityBeanConverter<User, UserBean> converter;

	@SuppressWarnings("unchecked")
	@Override
	public Page<UserBean> getItemsOnPage(Pageable pageable)
	{
		return userDao.findAll(pageable).map(new Converter<User, UserBean>() {
			@Override
			public UserBean convert(User user)
			{
				return converter.toBean(user, UserBean.class);
			}
		});
	}

	@Override
	public UserBean getUserByEmail(String email)
	{
		return converter.toBean(userDao.getByEmail(email), UserBean.class);
	}

	@Override
	public UserBean getUserById(Long userId)
	{
		return converter.toBean(userDao.findOne(userId), UserBean.class);
	}

	@Override
	public UserBean getUserByLogin(String login)
	{
		return converter.toBean(userDao.getByLogin(login), UserBean.class);
	}

	@Override
	public long count()	{ return userDao.count(); }

	@Override
	public void saveNewUser(UserBean user)
	{
		User entity	= converter.toEntity(user, User.class);
		entity.setPassword(Encryptor.INSTANCE.encrypt(entity.getPassword()));
		userDao.save(entity);
	}

	@Override
	public void saveUser(UserBean user)
	{
		userDao.save(converter.toEntity(user, User.class));
	}

	@Override
	public void deleteUser(Long userId)	{ userDao.delete(userId); }
}
