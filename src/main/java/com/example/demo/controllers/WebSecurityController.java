package com.example.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class WebSecurityController {

	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
//	@PostMapping("/logout")
//	public String logout() {
//		
//		return "redirect:/login?logout";
//	}
	
	@GetMapping("/test")
	public String getTestPage() {
		return "test_page";
	}
}
