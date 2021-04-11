package com.example.herokuboot;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.stereotype.Component;
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

	@Component
	public class CommandLineAppStartupRunner implements CommandLineRunner {
		private final Logger LOG = LoggerFactory.getLogger(CommandLineAppStartupRunner.class);

		@Autowired
		private StockRepository stockRepository;

		@Override
		public void run(String... args) throws Exception {
			ArrayList<Stocks> stock_list = new ArrayList<Stocks>();
			try (BufferedInputStream inputStream = new BufferedInputStream(
					new URL("https://www1.nseindia.com/content/equities/EQUITY_L.csv").openStream());) {
				byte data[] = inputStream.readAllBytes();
				String[] stock_data = (new String(data)).split("\n");
				for (int i = 1; i < stock_data.length; i++) {
					String[] stock_split = stock_data[i].split(",");
					Stocks stock = new Stocks();
					stock.setSymbol(stock_split[0]);
					stock.setName(stock_split[1]);
					stock_list.add(stock);
				}
			} catch (IOException e) {
				LOG.debug("exception handled");
				// handles IO exceptions
			}
			LOG.info("Added "+stock_list.size()+" stocks to the db.");
			stockRepository.saveAll(stock_list);
		}
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