package org.example.zzzyxwvut.armaria.domain.converters;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.example.zzzyxwvut.armaria.domain.naming.Constants.BOOKS;

@Converter(autoApply = true)
public class BooksConverter implements AttributeConverter<BOOKS, String>
{
	@Override
	public String convertToDatabaseColumn(BOOKS attribute)
	{
		return attribute.toString();
	}

	@Override
	public BOOKS convertToEntityAttribute(String dbData)
	{
		return BOOKS.valueOf(dbData.toUpperCase());
	}
}
