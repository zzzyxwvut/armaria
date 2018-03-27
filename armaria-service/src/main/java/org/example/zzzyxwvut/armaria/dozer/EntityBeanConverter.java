package org.example.zzzyxwvut.armaria.dozer;

import java.util.List;

public interface EntityBeanConverter<D, B>
{
	 B toBean(D entity, Class<B> beanClass);
	 List<B> toBeanList(Iterable<D> entities, Class<B> beanClass);

	 D toEntity(B bean, Class<D> entityClass);
}
