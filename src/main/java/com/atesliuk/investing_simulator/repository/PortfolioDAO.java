package com.atesliuk.investing_simulator.repository;

import com.atesliuk.investing_simulator.domain.Portfolio;
import org.springframework.data.repository.CrudRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

/**
 * DAO class, supports CRUD operations with Portfolio objects
 */
@CrossOrigin(origins = "http://localhost:4200")
public interface PortfolioDAO extends CrudRepository<Portfolio, Long> {
}
