package com.atesliuk.investing_simulator.repository;

import com.atesliuk.investing_simulator.domain.Portfolio;
import org.springframework.data.repository.CrudRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = "http://localhost:4200")
public interface PortfolioDAO extends CrudRepository<Portfolio, Long> {
}
