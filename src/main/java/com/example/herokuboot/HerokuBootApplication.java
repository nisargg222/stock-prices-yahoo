package com.example.herokuboot;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.repository.CrudRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@EntityScan("com.example.herokuboot")
@SpringBootApplication
public class HerokuBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(HerokuBootApplication.class, args);
	}

	@Bean
	ApplicationRunner applicationRunner(StockRepository stockRepository) {
		return args -> {
			ArrayList<Stocks> stock_list = new ArrayList<Stocks>();
			String allData = "";
			try (BufferedInputStream inputStream = new BufferedInputStream(
					new URL("https://www1.nseindia.com/content/equities/EQUITY_L.csv").openStream());) {
				byte data[] = new byte[1024];
				int byteContent;
				while ((byteContent = inputStream.read(data, 0, 1024)) != -1) {
//					System.out.println(new String(data));
//					System.out.println("....");
					allData = allData.concat(new String(data));
				}
				String[] stock_data = allData.split("\n");
				for(int i = 1;i<stock_data.length;i++) {
					String[] stock_split = stock_data[i].split(",");
					Stocks stock = new Stocks();
					stock.setSymbol(stock_split[0]);
					stock.setName(stock_split[1]);
					stock_list.add(stock);
				}
			} catch (IOException e) {
				// handles IO exceptions
			}
			System.out.print(stock_list);
			stockRepository.saveAll(stock_list);
		};
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

@Entity
class Stocks {
	@Id
	@GeneratedValue
	private Long id;
	private String symbol;
	private String name;
	private String price;

	public Stocks() {
	}

	public Long getId() {
		return id;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}
}

interface StockRepository extends CrudRepository<Stocks, Long> {
}