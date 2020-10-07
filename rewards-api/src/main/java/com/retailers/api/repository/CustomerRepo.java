package com.retailers.api.repository;


import org.springframework.data.repository.CrudRepository;

import com.retailers.api.model.CustomerInfo;

public interface CustomerRepo extends CrudRepository<CustomerInfo,Long> {
	
	public CustomerInfo findByCustomerId(int customerId);

}
