package com.example.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebSecurityController {

	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	@GetMapping("/test")
	public String getTestPage() {
		return "test_page";
	}
}
