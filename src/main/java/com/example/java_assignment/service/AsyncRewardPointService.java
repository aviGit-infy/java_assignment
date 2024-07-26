/**
 * This class is used to get the request asynchronous format which helps calling the thread to 
 * continue execute without blocking
 */
package com.example.java_assignment.service;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.example.java_assignment.entity.Transactions;
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
		List<Transactions> transactions = transactionsRepository.findByCustomerId(customerId);
		Map<String, Integer> monthlyPoints = new HashMap<>();
		int totalPoints = 0;
		for (Transactions transaction : transactions) {
			String month = new SimpleDateFormat("MMMM").format(transaction.getTransactionDate()).toLowerCase()
					.substring(0, 3);
			int points = rewardServiceImplementation.calculateRewardPoints(transaction.getAmount());
			monthlyPoints.put(month, monthlyPoints.getOrDefault(month, 0) + points);
			totalPoints += points;
		}
		RewardPointResponse response = new RewardPointResponse(monthlyPoints, totalPoints);
		return CompletableFuture.completedFuture(response);
	}

}
