package com.example.herokuboot.scheduler;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.example.herokuboot.entity.Stocks;
import com.example.herokuboot.repository.StockRepository;

@Configuration
@EnableScheduling
public class SymbolLoadScheduler {

	private final Logger LOGGER = LoggerFactory.getLogger(SymbolLoadScheduler.class);

	@Autowired
	private StockRepository stockRepository;

	@Scheduled(cron = "0 * * * * *")
	public void scheduleTaskUsingCronExpression() {

		LOGGER.info("Time");
		ArrayList<Stocks> stock_list = new ArrayList<Stocks>();
		try (BufferedInputStream inputStream = new BufferedInputStream(
				new URL("https://www1.nseindia.com/content/equities/EQUITY_L.csv").openStream());) {

			byte data[] = IOUtils.toByteArray(inputStream);
			String[] stock_data = (new String(data)).split("\n");

			for (int i = 1; i < stock_data.length; i++) {
				String[] stock_split = stock_data[i].split(",");

				Stocks stock = new Stocks();
				stock.setSymbol(stock_split[0]);
				stock.setName(stock_split[1]);
				stock_list.add(stock);
			}
			stockRepository.saveAll(stock_list);

			LOGGER.info("Added " + stock_list.size() + " stocks to the db.");
		} catch (IOException e) {
			LOGGER.debug("exception handled");
		}
	}
}