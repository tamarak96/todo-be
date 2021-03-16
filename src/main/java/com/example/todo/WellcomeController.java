package com.example.todo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WellcomeController {
	
	@GetMapping("/")
	public String welcome() {
		return "Welcome to Spring Boot";
	}
}
