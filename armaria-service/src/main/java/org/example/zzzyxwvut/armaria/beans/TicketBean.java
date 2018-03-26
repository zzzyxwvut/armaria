package org.example.zzzyxwvut.armaria.beans;

import java.util.Objects;

import org.example.zzzyxwvut.armaria.domain.naming.Constants.TICKETS;

public class TicketBean
{
	private Long id;
	private Integer bookId;
	private Long userId;
	private TICKETS status	= TICKETS.INVALID;
	private BookBean book;
	private UserBean user;

	@SuppressWarnings("unused")
	private Long item	= 0L;

	public TicketBean() { }

	public Long getId()			{ return id; }
	public void setId(Long id)		{ this.id	= id; }

	public Integer getBookId()		{ return bookId; }
	public void setBookId(Integer bookId)	{ this.bookId	= bookId; }

	public BookBean getBook()		{ return book; }
	public void setBook(BookBean book)	{ this.book	= book; }

	public Long getUserId()			{ return userId; }
	public void setUserId(Long userId)	{ this.userId	= userId; }

	public UserBean getUser()		{ return user; }
	public void setUser(UserBean user)	{ this.user	= user; }

	public TICKETS getStatus()		{ return status; }
	public void setStatus(TICKETS status) { this.status	= status; }

	@Override
	public String toString()
	{
		StringBuilder b	= new StringBuilder(256);
		b.append(super.toString())
			.append("\nid:\t")	.append(getId())
			.append("\nbook:\t")	.append(getBook())
			.append("\nuser:\t")	.append(getUser())
			.append("\nstatus:\t")	.append(getStatus());
		return b.toString();
	}

	@Override
	public final boolean equals(Object that)
	{
		if (this == that) {
			return true;
		} else if (!(that instanceof TicketBean)) {
			return false;
		}	/* instanceof allows LHS null [JLS9, 15.20.2] */

		final TicketBean t	= (TicketBean) that;
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
