package org.example.zzzyxwvut.armaria.service;

import org.example.zzzyxwvut.armaria.beans.LoanBean;

public interface LoanService extends GenericService
{
	Iterable<LoanBean> getAllLoans();

	long count();

	boolean hasLoan(Long loanId);

	void saveLoan(LoanBean loan);

	void deleteLoan(Long loanId);
}
