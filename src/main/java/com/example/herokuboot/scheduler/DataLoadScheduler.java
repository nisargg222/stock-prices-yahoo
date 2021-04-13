package com.example.herokuboot.scheduler;

import java.io.IOException;
import java.util.List;

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

	@Scheduled(cron = "0 * * * * *")
	public void priceLoadScheduler() throws IOException {
		List<Stocks> stockList = stockRepository.findAllStocks();
		LOGGER.warn("Start");
		for (int i = 0; i < stockList.size(); i++) {
			try {
				priceLoaderService.updatePrice(stockList.get(i));
			} catch (Exception e) {
			}
		}
		LOGGER.warn("Done");
	}
}