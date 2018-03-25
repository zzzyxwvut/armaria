package org.example.zzzyxwvut.armaria.domain.converters;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.example.zzzyxwvut.armaria.domain.naming.Constants.USERS;

@Converter(autoApply = true)
public class UsersConverter implements AttributeConverter<USERS, String>
{
	@Override
	public String convertToDatabaseColumn(USERS attribute)
	{
		return attribute.toString();
	}

	@Override
	public USERS convertToEntityAttribute(String dbData)
	{
		return USERS.valueOf(dbData.toUpperCase());
	}
}
