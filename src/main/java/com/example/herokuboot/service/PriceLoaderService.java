package com.example.herokuboot.service;

import java.io.IOException;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import com.example.herokuboot.entity.Stocks;
import com.example.herokuboot.repository.StockRepository;

import yahoofinance.Stock;

@Service
public class PriceLoaderService {

	@Autowired
	private StockRepository stockRepository;

	@Async
	public Future<String> updatePrice(Stocks stock) throws IOException {
		stock.setPrice((new Stock(stock.getSymbol() + ".NS")).getQuote(true).getPrice().toString());
		stockRepository.save(stock);
		return new AsyncResult<>("Done");
	}
}
