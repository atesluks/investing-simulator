package com.atesliuk.investing_simulator.service;

import com.atesliuk.investing_simulator.domain.Deal;
import com.atesliuk.investing_simulator.repository.DealDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Implementation of Deal Service. Handles connection
 * between Deal Controller and Deal DAO
 */
@Service
public class DealServiceImpl implements DealService {

    /*
    Autowiring Deal DAO for further usage
     */
    @Autowired
    private DealDAO dealDAO;

    /**
     * The method returns list of all Deals from the database
     * @return an Iteratable of all Deals
     */
    @Override
    public Iterable<Deal> getAllDeals() {
        return dealDAO.findAll();
    }

    /**
     * The method takes a Deal's id as an imput, and return the object of that deal
     * @param id - id of a deal
     * @return - the Deal object
     */
    @Override
    public Deal getDeal(Long id) {
        Optional<Deal> result = dealDAO.findById(id);
        if (result.isPresent()) return result.get();
        else return null;
    }

    /**
     * The method saves a new deal or updates an old one
     * @param theDeal - a Deal object to save
     * @return - an object that has been saved
     */
    @Override
    public Deal saveDeal(Deal theDeal) {
        return dealDAO.save(theDeal);
    }

    /**
     * The method deletes the passed Deal object from the database
     * @param theDeal - a Deal object that has to be deleted
     */
    @Override
    public void deleteDeal(Deal theDeal) {
        dealDAO.delete(theDeal);
    }
}
