package com.example.java_assignment.controller;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import com.example.java_assignment.repository.TransactionRepository;
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
	private TransactionRepository transactionRepository;

	@Autowired
	private RewardServiceImplementation rewardServiceImplementation;

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
			List<Transactions> transactions = transactionRepository.findByCustomerId(custId);
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
