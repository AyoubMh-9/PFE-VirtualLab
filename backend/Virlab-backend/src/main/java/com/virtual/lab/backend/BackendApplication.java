package com.virtual.lab.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class BackendApplication {


	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}


	/*
	@GetMapping("/api/heath-check")
	public String healthCheck(){
		return "ok";
	}
	
	 */

}
