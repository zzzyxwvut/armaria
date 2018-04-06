package org.example.zzzyxwvut.armaria.validators;

import java.util.Set;

import org.example.zzzyxwvut.armaria.beans.LoanBean;
import org.example.zzzyxwvut.armaria.beans.TicketBean;
import org.example.zzzyxwvut.armaria.beans.UserBean;
import org.example.zzzyxwvut.armaria.domain.naming.Constants.QUOTA;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class TicketQuotaValidator implements Validator
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
		Set<TicketBean> tickets	= user.getTickets();
		Set<LoanBean> loans	= user.getLoans();

		if (QUOTA.TICKETS.exceedsTicketQuota(tickets.size())) {
			errors.rejectValue("tickets", "quota.tickets");
		} else if ((QUOTA.LOANS.getValue() + QUOTA.TICKETS.getValue()) <=
					(loans.size() + tickets.size())) {
			/*
			 * Restrict the overall number of books borrowed and
			 * wait-listed not to exceed the cumulative quota.
			 */
			errors.rejectValue("tickets", "quota.total");
		}
	}
}
