package org.example.zzzyxwvut.armaria.domain.converters;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.example.zzzyxwvut.armaria.domain.naming.Constants.LOANS;

@Converter(autoApply = true)
public class LoansConverter implements AttributeConverter<LOANS, String>
{
	@Override
	public String convertToDatabaseColumn(LOANS attribute)
	{
		return attribute.toString();
	}

	@Override
	public LOANS convertToEntityAttribute(String dbData)
	{
		return LOANS.valueOf(dbData.toUpperCase());
	}
}
