package org.example.zzzyxwvut.armaria.dozer;

import org.dozer.DozerConverter;
import org.example.zzzyxwvut.armaria.beans.TesseraBean;
import org.example.zzzyxwvut.armaria.domain.Tessera;

public class TesseraBeanTesseraConverter extends DozerConverter<Tessera, TesseraBean>
{
	public TesseraBeanTesseraConverter()
	{
		super(Tessera.class, TesseraBean.class);
	}

	@Override
	public TesseraBean convertTo(Tessera source, TesseraBean destination)
	{
		destination.setEntry(source.getEntry());
		destination.setToken(source.getToken());
		return destination;
	}

	@Override
	public Tessera convertFrom(TesseraBean source, Tessera destination)
	{
		destination.setEntry(source.getEntry());
		destination.setToken(source.getToken());
		return destination;
	}
}
