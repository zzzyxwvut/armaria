package org.example.zzzyxwvut.armaria.validators;

import org.example.zzzyxwvut.armaria.beans.UserBean;
import org.example.zzzyxwvut.armaria.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class NewUserValidator implements Validator
{
	@Autowired
	private UserService userService;

	@Override
	public boolean supports(Class<?> clazz)
	{
		return UserBean.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors)
	{
		UserBean user	= (UserBean) target;
		ValidationUtils.rejectIfEmpty(errors, "login", "generic.empty");
		ValidationUtils.rejectIfEmpty(errors, "password", "generic.empty");
		ValidationUtils.rejectIfEmpty(errors, "email", "generic.empty");

		if (errors.hasFieldErrors("login")
				|| errors.hasFieldErrors("password")
				|| errors.hasFieldErrors("email"))
			return;

		if (!user.getLogin().matches("[a-zA-Z_]{4,}"))
			errors.rejectValue("login", "login.pattern");

		if (!user.getEmail().matches(".+@.+"))
			errors.rejectValue("email", "email.pattern");

		if (user.getLogin().length() < 4 || user.getLogin().length() > 255)
			errors.rejectValue("login", "generic.size",
				new Integer[] { 4, 255 }, "Out of bounds [4-255]");

		if (user.getPassword().length() < 8 || user.getPassword().length() > 255)
			errors.rejectValue("login", "generic.size",
				new Integer[] { 8, 255 }, "Out of bounds [8-255]");

		if (user.getEmail().length() < 3 || user.getEmail().length() > 255)
			errors.rejectValue("login", "generic.size",
				new Integer[] { 3, 255 }, "Out of bounds [3-255]");

		if (userService.getUserByLogin(user.getLogin()) != null)
			errors.rejectValue("login", "login.duplicate");

		if (userService.getUserByEmail(user.getEmail()) != null)
			errors.rejectValue("email", "email.duplicate");
	}
}
