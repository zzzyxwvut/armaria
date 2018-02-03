package org.example.zzzyxwvut.armaria.controllers;

import javax.servlet.http.HttpServletRequest;

import org.example.zzzyxwvut.armaria.handlers.DefaultExceptionModelAndView;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(produces = "text/html; charset=UTF-8")
public class ErrorController
{
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Exception.class)
	private ModelAndView handle(HttpServletRequest request, Exception e)
	{
		return DefaultExceptionModelAndView.populate(request,
				HttpStatus.INTERNAL_SERVER_ERROR,
				new ModelAndView("error"), e);
	}

	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	@RequestMapping(value = "/invalid")
	public ModelAndView invalid(HttpServletRequest request)
	{
		return DefaultExceptionModelAndView.populate(request,
				HttpStatus.NOT_FOUND,
				new ModelAndView("invalid"),
				new RuntimeException(HttpStatus.NOT_FOUND.getReasonPhrase()));
	}

	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@RequestMapping(value = "/error")
	public ModelAndView error(HttpServletRequest request)
	{
		return DefaultExceptionModelAndView.populate(request,
				HttpStatus.BAD_REQUEST,
				new ModelAndView("error"),
				new RuntimeException(HttpStatus.BAD_REQUEST.getReasonPhrase()));
	}
}
