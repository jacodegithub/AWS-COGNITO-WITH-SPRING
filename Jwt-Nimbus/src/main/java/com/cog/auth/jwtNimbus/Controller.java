package com.cog.auth.jwtNimbus;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

	@GetMapping("/")
	public String working() {
		return "working";
	}
	
	@GetMapping("/testing")
	public String testing() {
		return "it is working";
	}
}
