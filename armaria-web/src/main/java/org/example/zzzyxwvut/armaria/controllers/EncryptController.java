package org.example.zzzyxwvut.armaria.controllers;

import org.example.zzzyxwvut.armaria.crypto.Encryptor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@PreAuthorize("hasAuthority('patron')")
@RequestMapping(produces = MediaType.TEXT_HTML_VALUE)
public class EncryptController
{
	@GetMapping("/encrypt")
	public String encrypt()	{ return "encrypt"; }

	@PostMapping("/encryptor")
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
