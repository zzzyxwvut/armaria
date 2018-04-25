package org.example.zzzyxwvut.armaria.validators;

import java.util.regex.Pattern;

import org.example.zzzyxwvut.armaria.beans.UserBean;
import org.example.zzzyxwvut.armaria.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class OldUserValidator implements Validator
{
	@Autowired
	private UserService userService;

	private static final Pattern validEmail	= Pattern.compile(".+@.+");

	@Override
	public boolean supports(Class<?> clazz)
	{
		return UserBean.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors)
	{
		UserBean user	= (UserBean) target;

		/* Could be provided with a form:hidden tag. */
		if (user.getLogin() == null || user.getLogin().length() == 0)
			throw new IllegalStateException("Missing principal");

		/* Allow to change any following field or none. */
		boolean emptyEmail	= (user.getEmail() == null
					|| user.getEmail().length() == 0);
		boolean emptyPassword	= (user.getPassword() == null
					|| user.getPassword().length() == 0);

		if (!emptyPassword) {
			if (user.getPassword().length() < 8
					|| user.getPassword().length() > 255)
				errors.rejectValue("login", "generic.size",
						new Integer[] { 8, 255 },
						"Out of bounds [8-255]");
		}

		if (!emptyEmail) {
			if (!OldUserValidator.validEmail.matcher(user.getEmail()).matches())
				errors.rejectValue("email", "email.pattern");

			if ((user.getEmail().length() < 3
					|| user.getEmail().length() > 255))
				errors.rejectValue("login", "generic.size",
						new Integer[] { 3, 255 },
						"Out of bounds [3-255]");

			UserBean duplicate	= userService.getUserByEmail(user.getEmail());

			if (duplicate != null		/* Ye poachers! */
					&& !duplicate.getLogin().equals(user.getLogin()))
				errors.rejectValue("email", "email.duplicate");
		}
	}
}
