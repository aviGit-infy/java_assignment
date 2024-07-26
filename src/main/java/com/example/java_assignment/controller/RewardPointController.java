/**
 * This is the controller class which contains methods to create and fetch the customer rewards 
 */
package com.example.java_assignment.controller;

import java.util.concurrent.CompletableFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.java_assignment.entity.Transactions;
import com.example.java_assignment.model.RewardPointResponse;
import com.example.java_assignment.service.AsyncRewardPointService;
import com.example.java_assignment.service.RewardServiceImplementation;

/**
 * The RewardPointController class
 * 
 * @author avinash.menon
 *
 */
@RestController
@RequestMapping("/api/reward-points")
public class RewardPointController {

	@Autowired
	private RewardServiceImplementation rewardServiceImplementation;
	
	@Autowired
	private AsyncRewardPointService asyncRewardPointService;

	/**
	 * The getRewardPoints returns RewardPointResponse from the database
	 * 
	 * @param customerId
	 * @return
	 */
	@GetMapping("/{customerId}")
	public ResponseEntity<Object> getRewardPoints(@PathVariable String customerId) {
		try {
			Long custId = Long.parseLong(customerId);
			if (custId <= 0) {
				throw new IllegalArgumentException("Customer ID must be a positive number");
			}
			CompletableFuture<RewardPointResponse> future = asyncRewardPointService.getRewardPointsAsync(custId);
			RewardPointResponse response = future.get(); // Wait for the asynchronous task to complete
			return ResponseEntity.ok(response);
		} catch (NumberFormatException e) {
			return ResponseEntity.badRequest().body("Invalid customer ID format");
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while processing the request");
		}
	}

	/**
	 * The createTransaction helps to create new reward point transactions for
	 * customer
	 * 
	 * @param transaction
	 * @return
	 */
	@PostMapping("/createTransactions")
	public ResponseEntity<String> createTransaction(@RequestBody Transactions transaction) {
		try {
			rewardServiceImplementation.calculateAndStoreRewardPoints(transaction.getCustomerId(),
					transaction.getAmount(), transaction.getTransactionDate());
			return ResponseEntity.ok("Transaction created successfully");
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while processing the request");
		}

	}
}
