package com.retailers.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.retailers.api.model.CustomerInfo;
import com.retailers.api.model.CustomerRewards;
import com.retailers.api.repository.CustomerRepo;
import com.retailers.api.service.RewardsCalculator;


@RestController
public class CustomerRewardsApi {
	
	@Autowired
	RewardsCalculator rewardsCalculator;

    @Autowired
    CustomerRepo customerRepo;

    @GetMapping(value = "/{customerId}/rewards",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomerRewards> getRewardsByCustomerId(@PathVariable("customerId") Integer customerId){
        CustomerInfo customer = customerRepo.findByCustomerId(customerId);
        if(customer == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        CustomerRewards customerRewards = rewardsCalculator.getRewards(customerId);
        return new ResponseEntity<>(customerRewards,HttpStatus.OK);
    }

}
