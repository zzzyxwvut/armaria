package org.example.zzzyxwvut.armaria.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController
{
	private final Logger logger	= LogManager.getLogger();

	@ExceptionHandler(Exception.class)
	private String handle(Exception e)
	{
		logger.error(e.getMessage(), e);
		return "index";
	}

	@RequestMapping(value = {"/", "/index"}, produces = "text/html; charset=UTF-8")
	public String index()
	{
		return "index";
	}
}
