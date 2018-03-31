package org.example.zzzyxwvut.armaria.service;

import org.example.zzzyxwvut.armaria.beans.UserBean;

public interface UserService extends GenericService
{
	UserBean getUserByEmail(String email);

	UserBean getUserById(Long userId);

	UserBean getUserByLogin(String login);

	long count();

	void saveNewUser(UserBean user);

	void saveUser(UserBean user);

	void deleteUser(Long userId);
}
