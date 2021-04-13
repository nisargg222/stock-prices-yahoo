package com.example.herokuboot.service;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.herokuboot.entity.Stocks;
import com.example.herokuboot.repository.StockRepository;
import com.example.herokuboot.scheduler.DataLoadScheduler;

@Service
public class SymbolLoaderService {

	private final Logger LOGGER = LoggerFactory.getLogger(DataLoadScheduler.class);

	@Autowired
	private StockRepository stockRepository;

	public void populateSymbol() {
		if (stockRepository.findAllStocks().isEmpty()) {
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
				int stock_added = saveList(stock_list);

				LOGGER.info("Added " + stock_added + " stocks to the db.");
			} catch (IOException e) {
				LOGGER.debug("exception handled");
			}
		}
	}

	private int saveList(ArrayList<Stocks> stock_list) {
		int counter = 0;
		for (int i = 0; i < stock_list.size(); i++) {
			if (stockRepository.findBySymbol(stock_list.get(i).getSymbol()).isEmpty()) {
				stockRepository.save(stock_list.get(i));
				counter++;
			}
		}
		return counter;
	}
}
