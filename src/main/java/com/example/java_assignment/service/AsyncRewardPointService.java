/**
 * This class is used to get the request asynchronous format which helps calling the thread to 
 * continue execute without blocking
 */
package com.example.java_assignment.service;

import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.example.java_assignment.entity.Transactions;
import com.example.java_assignment.model.RewardPointForAllCustomer;
import com.example.java_assignment.model.RewardPointResponse;
import com.example.java_assignment.repository.TransactionRepository;

/**
 * The AsyncRewardPointService 
 * allowing the calling thread to continue executing without blocking.
 * @author avinash.menon
 *
 */
@Service
public class AsyncRewardPointService {

	@Autowired
	private TransactionRepository transactionsRepository;

	@Autowired
	private RewardServiceImplementation rewardServiceImplementation;

	/**
	 * The getRewardPointsAsync method returns a CompletableFuture object, which
	 * represents the result of the asynchronous operation. The calling thread can
	 * use this object to retrieve the result of the asynchronous operation when it
	 * completes.
	 * 
	 * @param customerId
	 * @return
	 */
	@Async
	public CompletableFuture<RewardPointResponse> getRewardPointsAsync(Long customerId) {
		List<Transactions> transactionsList = transactionsRepository.findByCustomerId(customerId);
		Map<String, Integer> monthlyPoints = new HashMap<>();
		int totalPoints = 0;
		for (Transactions transaction : transactionsList) {
			String month = transaction.getTransactionDate().getMonth().getDisplayName(TextStyle.SHORT,
					Locale.getDefault());
			int points = rewardServiceImplementation.calculateRewardPoints(transaction.getAmount());
			monthlyPoints.put(month, monthlyPoints.getOrDefault(month, 0) + points);
			totalPoints += points;
		}
		RewardPointResponse rewardPointResponse = new RewardPointResponse(monthlyPoints, totalPoints);
		return CompletableFuture.completedFuture(rewardPointResponse);
	}
	
	/**
	 * The below method will return all the customerId and will calculate and provide the CompletableFuture
	 * object, which represents the result of the asynchronous operation
	 * @return
	 */
	@Async
	public CompletableFuture<List<RewardPointForAllCustomer>> getRecordsForAllCustomers() {
		List<Transactions> transactionsList = transactionsRepository.findAll();
		Map<Long, Map<String, Integer>> customerPoints = new HashMap<>();
		for (Transactions transaction : transactionsList) {
			Long customerId = transaction.getCustomerId();
			String month = transaction.getTransactionDate().getMonth().getDisplayName(TextStyle.SHORT,
					Locale.getDefault());
			int points = rewardServiceImplementation.calculateRewardPoints(transaction.getAmount());
			customerPoints.putIfAbsent(customerId, new HashMap<>());
			// this will map the customer with its ID monthly reward points earned.
			customerPoints.get(customerId).put(month, customerPoints.get(customerId).getOrDefault(month, 0) + points);
		}

		List<RewardPointForAllCustomer> rewardPointForAllCustomerList = customerPoints.entrySet().stream()
				.map(entry -> new RewardPointForAllCustomer(entry.getKey(), entry.getValue(),
						entry.getValue().values().stream().mapToInt(Integer::intValue).sum()))
				.collect(Collectors.toList());

		return CompletableFuture.completedFuture(rewardPointForAllCustomerList);
	}
}
