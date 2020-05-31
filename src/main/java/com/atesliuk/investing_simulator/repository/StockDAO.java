package com.atesliuk.investing_simulator.repository;

import com.atesliuk.investing_simulator.domain.Stock;
import org.springframework.data.repository.CrudRepository;

public interface StockDAO extends CrudRepository<Stock, Long> {
}
