package com.atesliuk.investing_simulator.service;

import com.atesliuk.investing_simulator.domain.Deal;

/**
 * Interface for Deal Service
 */
public interface DealService {

    Iterable<Deal> getAllDeals();

    Deal getDeal(Long id);

    Deal saveDeal(Deal theDeal);

    void deleteDeal(Deal theDeal);

}
