package org.example.zzzyxwvut.armaria.domain.converters;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.example.zzzyxwvut.armaria.domain.naming.Constants.TICKETS;

@Converter(autoApply = true)
public class TicketsConverter implements AttributeConverter<TICKETS, String>
{
	@Override
	public String convertToDatabaseColumn(TICKETS attribute)
	{
		return attribute.toString();
	}

	@Override
	public TICKETS convertToEntityAttribute(String dbData)
	{
		return TICKETS.valueOf(dbData.toUpperCase());
	}
}
