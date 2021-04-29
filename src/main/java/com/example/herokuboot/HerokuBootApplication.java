package com.example.herokuboot;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.herokuboot.entity.Stocks;
import com.example.herokuboot.repository.StockRepository;
import com.example.herokuboot.service.SymbolLoaderService;

@EntityScan("com.example.herokuboot")
@SpringBootApplication
public class HerokuBootApplication implements CommandLineRunner {

	@Autowired
	SymbolLoaderService loaderService;

	public static void main(String[] args) {
		SpringApplication.run(HerokuBootApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		loaderService.populateSymbol();
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

	@GetMapping("/prices")
	List<Stocks> greetings() {
		return (List<Stocks>) stockRepository.findAll();
	}
}