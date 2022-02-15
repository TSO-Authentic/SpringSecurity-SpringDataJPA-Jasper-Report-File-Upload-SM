package com.ai.sm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class LoginController {

	@GetMapping("/")
	public ModelAndView login() {

		return new ModelAndView("LGN001");
	}
}
