package com.atesliuk.investing_simulator.controller;

import com.atesliuk.investing_simulator.controller.exceptions.EntityNotFoundException;
import com.atesliuk.investing_simulator.domain.Deal;
import com.atesliuk.investing_simulator.service.DealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api")
public class DealRestController {

    @Autowired
    private DealService dealService;

    @GetMapping("/deal")
    public Iterable<Deal> getAllDeals() {
        return dealService.getAllDeals();
    }

    @GetMapping("/deals/{dealId}")
    public Deal getDeal(@RequestBody Long dealId) {
        Deal theDeal = dealService.getDeal(dealId);
        if (theDeal == null) throw new EntityNotFoundException("Deal id not found - " + dealId);
        return theDeal;
    }

    @PostMapping("/deals")
    public Deal addDeal(@RequestBody Deal theDeal) {
        theDeal.setId(0L);
        theDeal.setDate(LocalDate.now());
        return dealService.saveDeal(theDeal);
    }

    @PutMapping("/deals")
    public Deal updateDeal(@RequestBody Deal theDeal) {
        return dealService.saveDeal(theDeal);
    }

    @DeleteMapping("/deals/{dealId}")
    public String deleteDeal(@RequestBody Long dealId) {
        Deal theDeal = dealService.getDeal(dealId);
        if (theDeal == null) {
            return "Error. User with id " + dealId + " was not found.";
        }
        dealService.deleteDeal(theDeal);
        return "Deleted user id - " + dealId;
    }

}
