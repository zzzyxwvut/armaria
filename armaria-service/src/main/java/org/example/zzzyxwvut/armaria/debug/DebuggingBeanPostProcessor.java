package org.example.zzzyxwvut.armaria.debug;

import java.util.concurrent.atomic.AtomicLong;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

public class DebuggingBeanPostProcessor implements BeanPostProcessor
{
	private final Logger logger	= LogManager.getLogger();
	private final AtomicLong count	= new AtomicLong(0L);

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName)
							throws BeansException
	{
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName)
							throws BeansException
	{
		logger.debug(new StringBuilder(512)
			.append(String.format("%08d : ", count.incrementAndGet()))
			.append(Thread.currentThread().getName()).append(" : [")
			.append(beanName).append("] : ")
			.append(bean.toString()));
		return bean;
	}
}
