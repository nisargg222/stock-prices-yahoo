package com.example.herokuboot.service;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.herokuboot.entity.Stocks;
import com.example.herokuboot.repository.StockRepository;

import yahoofinance.Stock;

@Service
public class PriceLoaderService {

	private final Logger LOGGER = LoggerFactory.getLogger(PriceLoaderService.class);

	@Autowired
	private StockRepository stockRepository;

	public void populatePrice() throws IOException {
		LOGGER.info("Start");
		List<Stocks> stockList = stockRepository.findAllStocks();
		for (int i = 0; i < stockList.size(); i++) {
			stockList.get(i).setPrice((new Stock(stockList.get(i).getSymbol() + ".NS")).getQuote(true).getPrice().toString());
		}
		stockRepository.saveAll(stockList);
		LOGGER.info("End");
	}
}
