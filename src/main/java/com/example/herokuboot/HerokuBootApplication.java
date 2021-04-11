package com.example.herokuboot;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.herokuboot.entity.Stocks;
import com.example.herokuboot.repository.StockRepository;

@EntityScan("com.example.herokuboot")
@SpringBootApplication
public class HerokuBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(HerokuBootApplication.class, args);
	}
}

@RestController
class HelloController {

	@Autowired
	private StockRepository stockRepository;

	@GetMapping("/")
	String hello() {
		return "Stocks";
	}

	@GetMapping("/greetings")
	List<Stocks> greetings() {
		return (List<Stocks>) stockRepository.findAll();
	}
}