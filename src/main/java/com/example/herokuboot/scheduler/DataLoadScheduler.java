package com.example.herokuboot.scheduler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Future;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.herokuboot.entity.Stocks;
import com.example.herokuboot.repository.StockRepository;
import com.example.herokuboot.service.PriceLoaderService;
import com.example.herokuboot.service.SymbolLoaderService;

@Component
@EnableScheduling
public class DataLoadScheduler {

	private final Logger LOGGER = LoggerFactory.getLogger(DataLoadScheduler.class);

	@Autowired
	SymbolLoaderService symbolLoaderService;

	@Autowired
	PriceLoaderService priceLoaderService;

	@Autowired
	private StockRepository stockRepository;

	@Scheduled(cron = "0 0 8 * * *", zone = "IST")
	public void symbolLoadScheduler() {
		symbolLoaderService.populateSymbol();
	}

	@Scheduled(cron = "* * 9-16 * * *", zone = "IST")
	public void priceLoadScheduler() throws IOException {
		List<Stocks> stockList = stockRepository.findAllStocks();
		Collection<Future<String>> results = new ArrayList<>(stockList.size());
		StopWatch watch = new StopWatch();
		watch.start();
		for (int i = 0; i < stockList.size(); i++) {
			try {
				results.add(priceLoaderService.updatePrice(stockList.get(i)));
			} catch (Exception e) {
				LOGGER.warn("Exception in gettingg price");
			}
		}
		waitForResults(results);
		watch.stop();
		LOGGER.info("Time Elapsed: " + watch.getTime());
	}

	private void waitForResults(Collection<Future<String>> results) {
		results.forEach(result -> {
			try {
				result.get();
			} catch (Exception e) {
				LOGGER.error("Error processing");
			}
		});
	}
}