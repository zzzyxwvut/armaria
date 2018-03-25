package org.example.zzzyxwvut.armaria.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embeddable;

import org.example.zzzyxwvut.armaria.domain.converters.LocalDateTimeConverter;

@Embeddable
public class Tessera implements Serializable
{
	private static final long serialVersionUID	= 2975536263289316352L;
	private String token;
	private LocalDateTime entry;

	public Tessera() { }

	@Column(name = "token", nullable = true)
	public String getToken()		{ return token; }
	public void setToken(String token)	{ this.token	= token; }

	@Convert(converter = LocalDateTimeConverter.class, disableConversion = false)
	@Column(name = "entry", nullable = true)
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
