package org.example.zzzyxwvut.armaria.beans;

import java.time.LocalDateTime;

public class TesseraBean
{
	private String token;
	private LocalDateTime entry;

	public TesseraBean() { }

	public String getToken()		{ return token; }
	public void setToken(String token)	{ this.token	= token; }

	public LocalDateTime getEntry()		{ return entry; }
	public void setEntry(LocalDateTime entry) { this.entry	= entry; }

	@Override
	public String toString()
	{
		StringBuilder b	= new StringBuilder(128);
		b.append(super.toString())
			.append("\ntoken:\t")	.append(getToken())
			.append("\nentry:\t")	.append(getEntry());
		return b.toString();
	}
}
