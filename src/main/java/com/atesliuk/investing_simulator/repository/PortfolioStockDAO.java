package com.atesliuk.investing_simulator.repository;

import com.atesliuk.investing_simulator.domain.PortfolioStock;
import org.springframework.data.repository.CrudRepository;

public interface PortfolioStockDAO extends CrudRepository<PortfolioStock, Long> {
}
