package com.atesliuk.investing_simulator.repository;

import com.atesliuk.investing_simulator.domain.Portfolio;
import org.springframework.data.repository.CrudRepository;

public interface PortfolioDAO extends CrudRepository<Portfolio, Long> {
}
