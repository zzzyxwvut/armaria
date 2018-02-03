package org.example.zzzyxwvut.armaria.controllers;

import org.example.zzzyxwvut.armaria.crypto.Encryptor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(produces = "text/html; charset=UTF-8")
public class EncryptController
{
	@RequestMapping(value = "/encrypt")
	public String encrypt()
	{
		return "encrypt";
	}

	@RequestMapping(value = "/encryptor")
	public String encryptor(@RequestParam(value = "plain", required = false)
					String plain, RedirectAttributes attr)
	{
		if (plain != null) {
			attr.addFlashAttribute("plain",	plain);
			attr.addFlashAttribute("encrypted",
						Encryptor.INSTANCE.encrypt(plain));
			return "redirect:/encrypt";
		}

		return "redirect:/error";
	}
}
