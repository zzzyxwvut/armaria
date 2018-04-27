package org.example.zzzyxwvut.armaria.handlers;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.data.domain.Page;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class SecureCredentialsInterceptor extends HandlerInterceptorAdapter
{
	private void setEmptyValue(BeanWrapper wrapper, String name)
	{
		try {
			if (wrapper.getPropertyValue(name) != null
					&& wrapper.isWritableProperty(name))
				wrapper.setPropertyValue(name, "");
		} catch (Exception consumed) {
		}
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response,
			Object handler, ModelAndView modelAndView) throws Exception
	{
		if (modelAndView == null)
			return;

		for (Map.Entry<String, Object> entry :
					modelAndView.getModel().entrySet()) {
			if (entry.getValue() instanceof Iterable<?>) {
				Iterable<?> pool	= (entry.getValue() instanceof Page<?>)
					? ((Page<?>) entry.getValue()).getContent()
					: (Iterable<?>) entry.getValue();

				for (Object o : pool) {
					BeanWrapper wrapper	= PropertyAccessorFactory
						.forBeanPropertyAccess(o);
					setEmptyValue(wrapper, "password");
					setEmptyValue(wrapper, "user.password");
				}
			} else {
				BeanWrapper wrapper	= PropertyAccessorFactory
					.forBeanPropertyAccess(entry.getValue());
				setEmptyValue(wrapper, "password");
				setEmptyValue(wrapper, "user.password");
			}
		}
	}
}
