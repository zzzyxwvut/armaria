package org.example.zzzyxwvut.armaria.handlers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class DefaultExceptionHandler
{
	private DefaultExceptionHandler() { }

	/**
	 * Handles any exception not managed by the ExceptionHandler methods,
	 * marked with the ResponseStatus annotation, of controller classes.
	 *
	 * @param request	the request object
	 * @param response	the response object
	 * @param e		the Exception object
	 * @return		the error ModelAndView object
	 * @throws Exception	if this exception object is marked with the
	 *				{@code @ResponseStatus} annotation
	 */
	@ExceptionHandler(value = Exception.class)
	private ModelAndView handle(HttpServletRequest request,
			HttpServletResponse response, Exception e) throws Exception
	{
		if (AnnotationUtils.findAnnotation(e.getClass(),
					ResponseStatus.class) != null)
			throw e;

		return	DefaultExceptionModelAndView.populate(request,
				HttpStatus.valueOf(response.getStatus()),
				new ModelAndView("error"), e);
	}
}
