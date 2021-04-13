package com.example.herokuboot.scheduler;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.herokuboot.service.PriceLoaderService;
import com.example.herokuboot.service.SymbolLoaderService;

@Component
@EnableScheduling
public class DataLoadScheduler {

	@Autowired
	SymbolLoaderService symbolLoaderService;

	@Autowired
	PriceLoaderService priceLoaderService;

	@Scheduled(cron = "0 0 8 * * *", zone = "IST")
	public void symbolLoadScheduler() {
		symbolLoaderService.populateSymbol();
	}

	@Scheduled(cron = "0 * * * * *")
	public void priceLoadScheduler() throws IOException {
		priceLoaderService.populatePrice();
	}
}