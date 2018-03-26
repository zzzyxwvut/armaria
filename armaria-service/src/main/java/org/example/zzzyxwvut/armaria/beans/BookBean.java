package org.example.zzzyxwvut.armaria.beans;

import java.util.Objects;

import org.example.zzzyxwvut.armaria.domain.naming.Constants.BOOKS;

public class BookBean
{
	private Integer id;
	private String author;
	private String title;
	private String subtitle;
	private BOOKS language	= BOOKS.GREEK;
	private int pages;
	private int year;
	private BOOKS status	= BOOKS.AVAILABLE;

	public BookBean() { }

	public Integer getId()			{ return id; }
	public void setId(Integer id)		{ this.id	= id; }

	public String getAuthor()		{ return author; }
	public void setAuthor(String author)	{ this.author	= author; }

	public String getTitle()		{ return title; }
	public void setTitle(String title)	{ this.title	= title; }

	public String getSubtitle()		{ return subtitle; }
	public void setSubtitle(String subtitle) { this.subtitle	= subtitle; }

	public BOOKS getLanguage()		{ return language; }
	public void setLanguage(BOOKS language)	{ this.language	= language; }

	public int getPages()			{ return pages; }
	public void setPages(int pages)		{ this.pages	= pages; }

	public int getYear()			{ return year; }
	public void setYear(int year)		{ this.year	= year; }

	public BOOKS getStatus()		{ return status; }
	public void setStatus(BOOKS status)	{ this.status	= status; }

	@Override
	public String toString()
	{
		StringBuilder b	= new StringBuilder(1024);
		b.append(super.toString())
			.append("\nid:\t")	.append(getId())
			.append("\nauthor:\t")	.append(getAuthor())
			.append("\ntitle:\t")	.append(getTitle())
			.append("\nsubtitle:\t").append(getSubtitle())
			.append("\nlanguage:\t").append(getLanguage())
			.append("\npages:\t")	.append(getPages())
			.append("\nyear:\t")	.append(getYear())
			.append("\nstatus:\t")	.append(getStatus());
		return b.toString();
	}

	@Override
	public final boolean equals(Object that)
	{
		if (this == that) {
			return true;
		} else if (!(that instanceof BookBean)) {
			return false;
		}	/* instanceof allows LHS null [JLS9, 15.20.2] */

		final BookBean t	= (BookBean) that;
		return (t.getYear() == getYear()
			&& t.getPages() == getPages()
			&& Objects.equals(t.getAuthor(), getAuthor())
			&& Objects.equals(t.getTitle(), getTitle()));
	}

	@Override
	public int hashCode()
	{
		int r	= 17;
		r	= (r << 5) - r + Objects.hashCode(getTitle());
		r	= (r << 5) - r + Objects.hashCode(getAuthor());
		r	= (r << 5) - r + getPages();
		r	= (r << 5) - r + getYear();
		return r;
	}
}
