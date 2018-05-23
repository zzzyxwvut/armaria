package org.example.zzzyxwvut.armaria.domain.converters;

import java.util.Locale;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class LocaleConverter implements AttributeConverter<Locale, String>
{
	@Override
	public String convertToDatabaseColumn(Locale attribute)
	{
		return attribute.toString();
	}

	@Override
	public Locale convertToEntityAttribute(String dbData)
	{
		String[] items	= dbData.split("_", 3);
		return (items.length == 3)
			? new Locale(items[0], items[1], items[2])
			: (items.length == 2)
				? new Locale(items[0], items[1])
				: new Locale(items[0]);
	}
}
