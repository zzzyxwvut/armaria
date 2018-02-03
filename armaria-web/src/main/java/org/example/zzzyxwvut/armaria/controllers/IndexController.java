package org.example.zzzyxwvut.armaria.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(produces = "text/html; charset=UTF-8")
public class IndexController
{
	@RequestMapping(value = {"/", "/index"})
	public String index()
	{
		return "index";
	}
}
