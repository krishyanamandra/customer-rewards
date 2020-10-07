package com.retailers.api.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import com.retailers.api.model.CustomerTransactions;


public interface CustomerTransactionRepo extends CrudRepository<CustomerTransactions,Integer> {
	
	public List<CustomerTransactions> findByCustomerId(Integer customerId);
	public List<CustomerTransactions> findAllByCustomerIdAndTransactionDateBetween(Integer customerId, Timestamp start,Timestamp end);
}
