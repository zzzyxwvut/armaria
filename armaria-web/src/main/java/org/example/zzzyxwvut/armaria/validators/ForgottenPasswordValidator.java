package org.example.zzzyxwvut.armaria.validators;

import org.example.zzzyxwvut.armaria.beans.UserBean;
import org.example.zzzyxwvut.armaria.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class ForgottenPasswordValidator implements Validator
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
		ValidationUtils.rejectIfEmpty(errors, "login", "generic.empty");
		ValidationUtils.rejectIfEmpty(errors, "email", "generic.empty");

		if (errors.hasFieldErrors("login") || errors.hasFieldErrors("email"))
			return;

		UserBean dummy	= (UserBean) target;
		UserBean user	= userService.getUserByLogin(dummy.getLogin());

		if (user == null || !user.getEmail().equals(dummy.getEmail()))
			errors.rejectValue("login", "generic.peek");
	}
}
