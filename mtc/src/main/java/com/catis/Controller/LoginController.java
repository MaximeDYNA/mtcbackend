package com.catis.Controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class LoginController {
	
	@RequestMapping("/api/v1/test")
	public String test(){
		return "<h1>Bonjour et bienvenue</h1>";
	}
}
