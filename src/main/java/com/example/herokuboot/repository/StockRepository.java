package com.example.herokuboot.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.herokuboot.entity.Stocks;

public interface StockRepository extends CrudRepository<Stocks, Long> {
}
