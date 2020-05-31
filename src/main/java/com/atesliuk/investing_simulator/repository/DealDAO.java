package com.atesliuk.investing_simulator.repository;

import com.atesliuk.investing_simulator.domain.Deal;
import org.springframework.data.repository.CrudRepository;

public interface DealDAO extends CrudRepository<Deal, Long> {
}
