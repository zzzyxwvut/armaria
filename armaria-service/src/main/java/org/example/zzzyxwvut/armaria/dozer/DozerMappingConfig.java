package org.example.zzzyxwvut.armaria.dozer;

import java.util.Collections;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.dozer.loader.api.BeanMappingBuilder;
import org.dozer.loader.api.FieldsMappingOptions;
import org.example.zzzyxwvut.armaria.beans.LoanBean;
import org.example.zzzyxwvut.armaria.beans.TicketBean;
import org.example.zzzyxwvut.armaria.beans.UserBean;
import org.example.zzzyxwvut.armaria.domain.Loan;
import org.example.zzzyxwvut.armaria.domain.Ticket;
import org.example.zzzyxwvut.armaria.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DozerMappingConfig
{
	@Autowired
	private Mapper mapper;

	private <D, B> Mapper dozerBeanMapper(Class<D> entity, Class<B> bean)
	{
		DozerBeanMapper mapper	= new DozerBeanMapper();
		mapper.addMapping(new BeanMappingBuilder() {
			@Override
			protected void configure()
			{
				mapping(entity, bean)
					/* Access @Version fields reflectively. */
					.fields(field("item").accessible(),
						field("item").accessible());
			}
		});			/* Provide custom java.time.* mappings. */
		mapper.setMappingFiles(Collections.singletonList("dozerJdk8Converters.xml"));
		return mapper;
	}

	private Mapper userBeanMapper()
	{
		DozerBeanMapper mapper	= new DozerBeanMapper();
		mapper.addMapping(new BeanMappingBuilder() {
			@Override
			protected void configure()
			{
				mapping(User.class, UserBean.class)
					.fields(field("item").accessible(),
						field("item").accessible())
					.fields("tessera", "tessera",
				FieldsMappingOptions.customConverter(
						TesseraBeanTesseraConverter.class));
			}
		});
		mapper.setMappingFiles(Collections.singletonList("dozerJdk8Converters.xml"));
		return mapper;
	}

	@Bean
	public Mapper bookMapper()	{ return mapper; }

	@Bean
	public Mapper loanMapper()	{ return dozerBeanMapper(Loan.class, LoanBean.class); }

	@Bean
	public Mapper ticketMapper()	{ return dozerBeanMapper(Ticket.class, TicketBean.class); }

	@Bean
	public Mapper userMapper()	{ return userBeanMapper(); }
}
