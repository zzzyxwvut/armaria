package org.example.zzzyxwvut.armaria.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.zzzyxwvut.armaria.crypto.Encryptor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class EncryptController
{
	private final Logger logger	= LogManager.getLogger();

	@ExceptionHandler(Exception.class)
	private String handle(Exception e)
	{
		logger.error(e.getMessage(), e);
		return "redirect:/index";
	}

	@RequestMapping(value = "/encrypt")
	public String encrypt()
	{
		return "encrypt";
	}

	@RequestMapping(value = "/encryptor")
	public String encryptor(@RequestParam(value = "plain") String plain,
						RedirectAttributes attr)
	{
		attr.addFlashAttribute("plain",	plain);
		attr.addFlashAttribute("encrypted",
					Encryptor.INSTANCE.encrypt(plain));
		return "redirect:/encrypt";
	}
}
