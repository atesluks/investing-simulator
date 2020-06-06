package com.atesliuk.investing_simulator.repository;

import com.atesliuk.investing_simulator.domain.Deal;
import org.springframework.data.repository.CrudRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = "http://localhost:4200")
public interface DealDAO extends CrudRepository<Deal, Long> {
}
