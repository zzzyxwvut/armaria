package org.example.zzzyxwvut.armaria.domain;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.example.zzzyxwvut.armaria.domain.converters.BooksConverter;
import org.example.zzzyxwvut.armaria.domain.naming.Constants.BOOKS;

@Entity
@Table(name = "books")
public class Book implements Serializable
{
	private static final long serialVersionUID	= 3463520264270468058L;
	private Integer id;
	private String author;
	private String title;
	private String subtitle;
	private BOOKS language	= BOOKS.GREEK;
	private int pages;
	private int year;
	private BOOKS status	= BOOKS.AVAILABLE;

	public Book() { }

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", length = 5, nullable = false)
	public Integer getId()			{ return id; }
	public void setId(Integer id)		{ this.id	= id; }

	@Column(name = "author", length = 255, nullable = false)
	public String getAuthor()		{ return author; }
	public void setAuthor(String author)	{ this.author	= author; }

	@Column(name = "title", length = 511, nullable = false)
	public String getTitle()		{ return title; }
	public void setTitle(String title)	{ this.title	= title; }

	@Column(name = "subtitle", length = 511, nullable = true)
	public String getSubtitle()		{ return subtitle; }
	public void setSubtitle(String subtitle) { this.subtitle	= subtitle; }

	@Convert(converter = BooksConverter.class, disableConversion = false)
	@Column(name = "language", nullable = false)
	public BOOKS getLanguage()		{ return language; }
	public void setLanguage(BOOKS language)	{ this.language	= language; }

	@Column(name = "pages", length = 4, nullable = false)
	public int getPages()			{ return pages; }
	public void setPages(int pages)		{ this.pages	= pages; }

	@Column(name = "year", length = 4, nullable = false)
	public int getYear()			{ return year; }
	public void setYear(int year)		{ this.year	= year; }

	@Convert(converter = BooksConverter.class, disableConversion = false)
	@Column(name = "status", nullable = false)
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
		} else if (!(that instanceof Book)) {
			return false;
		}	/* instanceof allows LHS null [JLS9, 15.20.2] */

		final Book t	= (Book) that;
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
