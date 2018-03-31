package org.example.zzzyxwvut.armaria.service.impl;

import org.example.zzzyxwvut.armaria.beans.LoanBean;
import org.example.zzzyxwvut.armaria.dao.LoanDao;
import org.example.zzzyxwvut.armaria.domain.Loan;
import org.example.zzzyxwvut.armaria.dozer.EntityBeanConverter;
import org.example.zzzyxwvut.armaria.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LoanServiceImpl implements LoanService
{
	@Autowired
	private LoanDao loanDao;

	@Autowired
	private EntityBeanConverter<Loan, LoanBean> converter;

	@SuppressWarnings("unchecked")
	@Override
	public Page<LoanBean> getItemsOnPage(Pageable pageable)
	{
		return loanDao.findAll(pageable).map(new Converter<Loan, LoanBean>() {
			@Override
			public LoanBean convert(Loan loan)
			{
				return converter.toBean(loan, LoanBean.class);
			}
		});
	}

	@Override
	public Iterable<LoanBean> getAllLoans()
	{
		return converter.toBeanList(loanDao.findAll(), LoanBean.class);
	}

	@Override
	public long count()	{ return loanDao.count(); }

	@Override
	public boolean hasLoan(Long loanId)	{ return loanDao.exists(loanId); }

	@Override
	public void saveLoan(LoanBean loan)
	{
		loanDao.save(converter.toEntity(loan, Loan.class));
	}

	@Override
	public void deleteLoan(Long loanId) { loanDao.delete(loanId); }
}
