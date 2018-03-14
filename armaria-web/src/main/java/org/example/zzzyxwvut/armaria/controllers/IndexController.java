package org.example.zzzyxwvut.armaria.controllers;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(produces = MediaType.TEXT_HTML_VALUE)
public class IndexController
{
	@GetMapping({"/", "/index", "/main"})
	public String index()	{ return "index"; }
}
