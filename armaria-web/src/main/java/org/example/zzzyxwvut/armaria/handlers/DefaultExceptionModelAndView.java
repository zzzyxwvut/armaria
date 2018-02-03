package org.example.zzzyxwvut.armaria.handlers;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.ModelAndView;

public class DefaultExceptionModelAndView
{
	private static final Logger logger	= LogManager.getLogger();

	private DefaultExceptionModelAndView() { }

	/**
	 * Adds to the supplied ModelAndView object the following keys and values.
	 *
	 *<pre>
	 *	exception	the Exception object
	 *	url		the URL used by the client to make the request
	 *	code		the integer value of the status code
	 *</pre>
	 *
	 * @param request	the request object
	 * @param status	the HTTP status code object
	 * @param mav		the ModelAndView object
	 * @param e		the Exception object
	 * @return		the modified ModelAndView object
	 */
	public static ModelAndView populate(HttpServletRequest request,
			HttpStatus status, ModelAndView mav, Exception e)
	{
		DefaultExceptionModelAndView.logger.error(e.getMessage(), e);
		mav.addObject("exception", e);
		mav.addObject("url",	request.getRequestURL());
		mav.addObject("code",	status.value());
		return mav;
	}
}
