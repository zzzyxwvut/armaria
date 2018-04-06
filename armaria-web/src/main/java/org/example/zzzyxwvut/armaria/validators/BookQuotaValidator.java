package org.example.zzzyxwvut.armaria.validators;

import java.util.Set;

import org.example.zzzyxwvut.armaria.beans.LoanBean;
import org.example.zzzyxwvut.armaria.beans.UserBean;
import org.example.zzzyxwvut.armaria.domain.naming.Constants.QUOTA;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class BookQuotaValidator implements Validator
{
	@Override
	public boolean supports(Class<?> clazz)
	{
		return UserBean.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors)
	{
		UserBean user		= (UserBean) target;
		Set<LoanBean> loans	= user.getLoans();

		if (QUOTA.LOANS.exceedsLoanQuota(loans.size()))
			errors.rejectValue("loans", "quota.loans");
	}
}
