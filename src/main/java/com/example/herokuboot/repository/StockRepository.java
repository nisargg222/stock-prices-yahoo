package com.example.herokuboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.herokuboot.entity.Stocks;

public interface StockRepository extends CrudRepository<Stocks, Long> {

	List<Stocks> findBySymbol(String symbol);

	@Query(value = "SELECT * FROM stocks", nativeQuery = true)
	List<Stocks> findAllStocks();
}