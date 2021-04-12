package com.example.herokuboot.service;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;

@Service
public class PriceLoaderService {

	private final Logger LOGGER = LoggerFactory.getLogger(PriceLoaderService.class);
	
	public void populatePrice() throws IOException{
		LOGGER.info("Start");
		Stock stock = YahooFinance.get("TCS.NS");
		LOGGER.info(stock.getQuote().getPrice().toString());
	}
	public void populatePrice1() throws IOException{
		LOGGER.info("Start");
		Document doc = Jsoup.connect("https://finance.yahoo.com/quote/TCS.NS?p=TCS.NS").get();
		Elements elm = doc.getElementsByClass("Trsdu(0.3s) Fw(b) Fz(36px) Mb(-4px) D(ib)");
		String price = elm.first().childNode(0).toString();
		price=price.replace(",", "");
		LOGGER.info(price);
	}
	public void populatePrice2() throws IOException{
		LOGGER.info("Start");
		Stock stock2 = new Stock("TCS.NS");
		LOGGER.info(stock2.getQuote(true).getPrice().toString());
	}
}
