package com.example.herokuboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class HerokuBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(HerokuBootApplication.class, args);
	}

}

@RestController
class HelloController {

	@GetMapping("/")
	String hello() {
		return "Stocks";
	}
}