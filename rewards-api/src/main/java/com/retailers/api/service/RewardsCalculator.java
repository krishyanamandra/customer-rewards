package com.retailers.api.service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.retailers.api.model.CustomerRewards;
import com.retailers.api.model.CustomerTransactions;
import com.retailers.api.repository.CustomerTransactionRepo;

@Service
public class RewardsCalculator {
	
	@Autowired
	CustomerTransactionRepo transactionRepo;
	
	@Autowired
	CustomerRewards customerrewards;
	
	@Autowired
	CustomerTransactions transactions;
	
	public final int singleRewardThreshold = 50;
	
	public final int doubleRewardThreshold = 100;
	
	public final int avgCalendarDays = 30;
	
	public CustomerRewards getRewards(Integer customerId) {

        Timestamp lastMonthTimestamp = getDateBeforeOffSetDays(avgCalendarDays);
        Timestamp lastSecondMonthTimestamp = getDateBeforeOffSetDays(avgCalendarDays + 30);
        Timestamp lastThirdMonthTimestamp = getDateBeforeOffSetDays(avgCalendarDays + 60);

        List<CustomerTransactions> lastMonthTransactions = transactionRepo.findAllByCustomerIdAndTransactionDateBetween(customerId,lastMonthTimestamp,Timestamp.from(Instant.now()));
        List<CustomerTransactions> lastSecondMonthTransactions = transactionRepo.findAllByCustomerIdAndTransactionDateBetween(customerId,lastSecondMonthTimestamp,lastMonthTimestamp);
        List<CustomerTransactions> lastThirdMonthTransactions = transactionRepo.findAllByCustomerIdAndTransactionDateBetween(customerId,lastThirdMonthTimestamp,lastSecondMonthTimestamp);

        Long lastMonthRewards = getRewardsPerMonth(lastMonthTransactions);
        Long lastSecondMonthRewards = getRewardsPerMonth(lastSecondMonthTransactions);
        Long lastThirdMonthRewards = getRewardsPerMonth(lastThirdMonthTransactions);

        CustomerRewards customerRewards = new CustomerRewards();
        customerRewards.setCustomerId(customerId);
        customerRewards.setLastMonthRewards(lastMonthRewards);
        customerRewards.setLastSecondMonthRewards(lastSecondMonthRewards);
        customerRewards.setLastThirdMonthRewards(lastThirdMonthRewards);
        customerRewards.setTotalRewards(lastMonthRewards+lastSecondMonthRewards+lastThirdMonthRewards);

        return customerRewards;

    }

    private Long getRewardsPerMonth(List<CustomerTransactions> transactions) {
        return transactions.stream().map(transaction -> calculateRewards(transaction)).
                collect(Collectors.summingLong(r->r.longValue()));
    }

    private Long calculateRewards(CustomerTransactions t) {
        if(t.getTransactionAmount() > singleRewardThreshold && t.getTransactionAmount() <= doubleRewardThreshold){
            return (long) Math.round(t.getTransactionAmount() - singleRewardThreshold);
        }else if(t.getTransactionAmount() > doubleRewardThreshold){
            return (long) (Math.round(t.getTransactionAmount() - doubleRewardThreshold)*2 + (doubleRewardThreshold - singleRewardThreshold));
        }else
            return 0l;

    }

    public Timestamp getDateBeforeOffSetDays(int offSet) {
        return Timestamp.valueOf(LocalDateTime.now().minusDays(offSet));
    }

}
