package org.example.zzzyxwvut.armaria.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import org.example.zzzyxwvut.armaria.domain.converters.LoansConverter;
import org.example.zzzyxwvut.armaria.domain.naming.Constants.LOANS;
import org.example.zzzyxwvut.armaria.domain.naming.Constants.QUOTA;

@Entity
@Access(AccessType.PROPERTY)
@Table(name = "loans")
public class Loan implements Serializable
{
	private static final long serialVersionUID	= 3444565072077345895L;
	private Long id;
	private Integer bookId;
	private Long userId;
	private Timestamp term	= new Timestamp(System.currentTimeMillis()
					+ QUOTA.DAYS_AS_MILLISEC.getValue());
	private LOANS status	= LOANS.DEFAULT;
	private Book book;
	private User user;

	@Access(AccessType.FIELD)
	@Version
	@Column(name = "item", nullable = false)
	private Long item	= 0L;

	public Loan() { }

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	public Long getId()			{ return id; }
	public void setId(Long id)		{ this.id	= id; }

	@Column(name = "book_id", nullable = false, insertable = false, updatable = false)
	public Integer getBookId()		{ return bookId; }
	public void setBookId(Integer bookId)	{ this.bookId	= bookId; }

	@ManyToOne(optional = false)
	@JoinColumn(name = "book_id", referencedColumnName = "id")
	public Book getBook()			{ return book; }
	public void setBook(Book book)		{ this.book	= book; }

	@Column(name = "user_id", nullable = false, insertable = false, updatable = false)
	public Long getUserId()			{ return userId; }
	public void setUserId(Long userId)	{ this.userId	= userId; }

	@ManyToOne(optional = false)
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	public User getUser()			{ return user; }
	public void setUser(User user)		{ this.user	= user; }

	@Column(name = "term", nullable = false)
	public Timestamp getTerm()		{ return new Timestamp(term.getTime()); }
	public void setTerm(Timestamp term)	{ this.term	= new Timestamp(term.getTime()); }

	@Convert(converter = LoansConverter.class, disableConversion = false)
	@Column(name = "status", nullable = false)
	public LOANS getStatus()		{ return status; }
	public void setStatus(LOANS status)	{ this.status	= status; }

	@Override
	public String toString()
	{
		StringBuilder b	= new StringBuilder(256);
		b.append(super.toString())
			.append("\nid:\t")	.append(getId())
			.append("\nbook:\t")	.append(getBook())
			.append("\nuser:\t")	.append(getUser())
			.append("\nterm:\t")	.append(getTerm())
			.append("\nstatus:\t")	.append(getStatus());
		return b.toString();
	}

	@Override
	public final boolean equals(Object that)
	{
		if (this == that) {
			return true;
		} else if (!(that instanceof Loan)) {
			return false;
		}	/* instanceof allows LHS null [JLS9, 15.20.2] */

		final Loan t	= (Loan) that;
		return (t.getBookId().intValue() == getBookId().intValue()
			&& t.getUserId().longValue() == getUserId().longValue());
	}

	@Override
	public int hashCode()
	{
		int r	= 17;
		r	= (r << 5) - r + Objects.hashCode(getBookId());
		r	= (r << 5) - r + ((getUserId() == null) ? 0
				: (int) (getUserId() ^ (getUserId() >>> 32)));
		return r;
	}
}
