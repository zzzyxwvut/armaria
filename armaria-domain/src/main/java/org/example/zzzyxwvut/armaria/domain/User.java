package org.example.zzzyxwvut.armaria.domain;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.example.zzzyxwvut.armaria.domain.converters.LocaleConverter;
import org.example.zzzyxwvut.armaria.domain.converters.UsersConverter;
import org.example.zzzyxwvut.armaria.domain.naming.Constants.USERS;

@Entity
@Access(AccessType.PROPERTY)
@Table(name = "users")
public class User implements Serializable
{
	private static final long serialVersionUID	= 5268265577847242002L;
	private Long id;
	private String login;
	private String password;
	private String email;
	private Locale locale	= Locale.US;
	private USERS role	= USERS.CLIENT;
	private USERS status	= USERS.INVALID;
	private Set<Loan> loans		= new LinkedHashSet<>();
	private Set<Ticket> tickets	= new LinkedHashSet<>();

	@Embedded
	private Tessera tessera	= new Tessera();

	public Tessera getTessera()		{ return tessera; }
	public void setTessera(Tessera tessera)	{ this.tessera	= tessera; }

	@Access(AccessType.FIELD)
	@Version
	@Column(name = "item", nullable = false)
	private Long item	= 0L;

	public User() { }

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	public Long getId()			{ return id; }
	public void setId(Long id)		{ this.id	= id; }

	@NotNull(message = "{generic.empty}")
	@Size(min = 4, max = 255, message = "{generic.size}")
	@Pattern(regexp = "[a-zA-Z_]{4,}", message = "{login.pattern}")
	@Column(name = "login", length = 255, nullable = false, unique = true)
	public String getLogin()		{ return login; }
	public void setLogin(String login)	{ this.login	= login; }

	@NotNull(message = "{generic.empty}")
	@Size(min = 8, max = 255, message = "{generic.size}")
	@Column(name = "password", length = 255, nullable = false)
	public String getPassword()		{ return password; }
	public void setPassword(String password) { this.password = password; }

	@NotNull(message = "{generic.empty}")
	@Size(min = 3, max = 255, message = "{generic.size}")
	@Email(regexp = ".+@.+", message = "{email.pattern}")
	@Column(name = "email", length = 255, nullable = false, unique = true)
	public String getEmail()		{ return email; }
	public void setEmail(String email)	{ this.email	= email; }

	@NotNull(message = "{generic.empty}")
	@Convert(converter = LocaleConverter.class, disableConversion = false)
	@Column(name = "locale", length = 32, nullable = false)
	public Locale getLocale()		{ return locale; }
	public void setLocale(Locale locale)	{ this.locale	= locale; }

	@NotNull
	@Convert(converter = UsersConverter.class, disableConversion = false)
	@Column(name = "role", nullable = false)
	public USERS getRole()			{ return role; }
	public void setRole(USERS role)		{ this.role	= role; }

	@NotNull
	@Convert(converter = UsersConverter.class, disableConversion = false)
	@Column(name = "status", nullable = false)
	public USERS getStatus()		{ return status; }
	public void setStatus(USERS status)	{ this.status	= status; }

	@OrderBy
	@OneToMany(mappedBy = "user", targetEntity = Loan.class)
	public Set<Loan> getLoans()		{ return loans; }
	public void setLoans(Set<Loan> loans)	{ this.loans	= loans; }

	@OrderBy
	@OneToMany(mappedBy = "user", targetEntity = Ticket.class)
	public Set<Ticket> getTickets()		{ return tickets; }
	public void setTickets(Set<Ticket> tickets) { this.tickets = tickets; }

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
		} else if (!(that instanceof User)) {
			return false;
		}	/* instanceof allows LHS null [JLS9, 15.20.2] */

		final User t	= (User) that;
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
