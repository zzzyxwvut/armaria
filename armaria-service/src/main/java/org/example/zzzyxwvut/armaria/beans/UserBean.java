package org.example.zzzyxwvut.armaria.beans;

import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

import org.example.zzzyxwvut.armaria.domain.naming.Constants.USERS;

public class UserBean
{
	private Long id;
	private String login;
	private String password;
	private String email;
	private Locale locale	= Locale.US;
	private USERS role	= USERS.CLIENT;
	private USERS status	= USERS.INVALID;
	private Set<LoanBean> loans	= new LinkedHashSet<>();
	private Set<TicketBean> tickets	= new LinkedHashSet<>();
	private TesseraBean tessera	= new TesseraBean();

	public TesseraBean getTessera()		{ return tessera; }
	public void setTessera(TesseraBean tessera) { this.tessera = tessera; }

	@SuppressWarnings("unused")
	private Long item	= 0L;

	public UserBean() { }

	public Long getId()			{ return id; }
	public void setId(Long id)		{ this.id	= id; }

	public String getLogin()		{ return login; }
	public void setLogin(String login)	{ this.login	= login; }

	public String getPassword()		{ return password; }
	public void setPassword(String password) { this.password = password; }

	public String getEmail()		{ return email; }
	public void setEmail(String email)	{ this.email	= email; }

	public Locale getLocale()		{ return locale; }
	public void setLocale(Locale locale)	{ this.locale	= locale; }

	public USERS getRole()			{ return role; }
	public void setRole(USERS role)		{ this.role	= role; }

	public USERS getStatus()		{ return status; }
	public void setStatus(USERS status)	{ this.status	= status; }

	public Set<LoanBean> getLoans()		{ return loans; }
	public void setLoans(Set<LoanBean> loans) { this.loans	= loans; }

	public Set<TicketBean> getTickets()	{ return tickets; }
	public void setTickets(Set<TicketBean> tickets) { this.tickets = tickets; }

	@Override
	public String toString()
	{
		StringBuilder b	= new StringBuilder(256);
		b.append(super.toString())
			.append("\nid:\t")	.append(getId())
			.append("\nlogin:\t")	.append(getLogin())
			.append("\npassword:\t").append(getPassword())
			.append("\nemail:\t")	.append(getEmail())
			.append("\nlocale:\t")	.append(getLocale())
			.append("\nrole:\t")	.append(getRole())
			.append("\nstatus:\t")	.append(getStatus())
			.append("\ntessera:\t")	.append(getTessera());
		return b.toString();
	}

	@Override
	public final boolean equals(Object that)
	{
		if (this == that) {
			return true;
		} else if (!(that instanceof UserBean)) {
			return false;
		}	/* instanceof allows LHS null [JLS9, 15.20.2] */

		final UserBean t	= (UserBean) that;
		return Objects.equals(t.getLogin(), getLogin());
	}

	@Override
	public int hashCode()
	{
		int r	= 17;
		r	= (r << 5) - r + Objects.hashCode(getLogin());
		return r;
	}
}
