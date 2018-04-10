package org.example.zzzyxwvut.armaria.controllers;

import javax.servlet.http.HttpServletRequest;

import org.example.zzzyxwvut.armaria.handlers.DefaultExceptionModelAndView;
import org.example.zzzyxwvut.armaria.security.UserCredentialsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(produces = MediaType.TEXT_HTML_VALUE)
public class ErrorController
{
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Exception.class)
	public ModelAndView handle(HttpServletRequest request, Exception e)
	{
		return DefaultExceptionModelAndView.populate(request,
			HttpStatus.INTERNAL_SERVER_ERROR, new ModelAndView("error"), e);
	}

	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@GetMapping("/reject")
	public ModelAndView reject(HttpServletRequest request,
						UserCredentialsException e)
	{
		return DefaultExceptionModelAndView.populate(request,
			HttpStatus.UNAUTHORIZED, new ModelAndView("reject"), e);
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@GetMapping("/invalid")
	public ModelAndView invalid(HttpServletRequest request)
	{
		return DefaultExceptionModelAndView.populate(request,
			HttpStatus.NOT_FOUND, new ModelAndView("invalid"),
			new RuntimeException(HttpStatus.NOT_FOUND.getReasonPhrase()));
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@GetMapping("/error")
	public ModelAndView error(HttpServletRequest request)
	{
		return DefaultExceptionModelAndView.populate(request,
			HttpStatus.BAD_REQUEST, new ModelAndView("error"),
			new RuntimeException(HttpStatus.BAD_REQUEST.getReasonPhrase()));
	}
}
