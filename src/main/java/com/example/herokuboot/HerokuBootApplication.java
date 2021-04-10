package com.example.herokuboot;

import java.util.List;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.Entity;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.repository.CrudRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SuppressWarnings("deprecation")
@SpringBootApplication
public class HerokuBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(HerokuBootApplication.class, args);
	}

	@Bean
	ApplicationRunner applicationRunner(GreetingRepository greetingRepository) {
		return args -> {
			greetingRepository.save(new Greeting("Hello"));
			greetingRepository.save(new Greeting("Hey"));
		};
	}
}

@RestController
class HelloController {

	private GreetingRepository greetingRepository;

	@GetMapping("/")
	String hello() {
		return "Stocks";
	}

	@GetMapping("/greetings")
	List<Greeting> greetings() {
		return (List<Greeting>) greetingRepository.findAll();
	}
}

@SuppressWarnings("deprecation")
@Entity
class Greeting {
	@Id
	@GeneratedValue
	private Long id;

	private String message;

	public Greeting() {
	}

	public Long getId() {
		return id;
	}

	public String getMessage() {
		return message;
	}

	public Greeting(String message) {
		this.message = message;
	}
}

interface GreetingRepository extends CrudRepository<Greeting, Long> {
}