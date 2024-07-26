package com.example.java_assignment.service;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.java_assignment.entity.Transactions;
import com.example.java_assignment.repository.TransactionRepository;


/**
 * The class RewardServiceImplementation
 * @author avinash.menon
 *
 */
@Service
public class RewardServiceImplementation implements RewardService {

	@Autowired
	private TransactionRepository transactionsRepository;

	@Override
	public int calculateRewardPoints(Double transactionAmount) {
		int points = 0;

		// Calculate points for amount spent over $100
		if (transactionAmount > 100) {
			double amountOver100 = transactionAmount - 100;
			points += amountOver100 * 2; // 2 points for every dollar over $100
		}

		// Calculate points for amount spent between $50 and $100
		if (transactionAmount > 50) {
			double amountBetween50And100 = Math.min(transactionAmount, 100) - 50;
			points += amountBetween50And100; // 1 point for every dollar between $50 and $100
		}

		return points;
	}

	/**
	 * Process to create a new customer records
	 * 
	 * @param customerId
	 * @param amount
	 * @param transactionDate
	 */
	public void calculateAndStoreRewardPoints(Long customerId, Double amount, Date transactionDate) {
		validateTransaction(customerId, amount, transactionDate);
		int points = calculateRewardPoints(amount);
		Transactions customerRewardPoints = new Transactions();
		customerRewardPoints.setCustomerId(customerId);
		customerRewardPoints.setAmount(points);
		customerRewardPoints.setTransactionDate(transactionDate);
		transactionsRepository.save(customerRewardPoints);
	}

	/**
	 * this method is for validating the user inputs
	 * 
	 * @param customerId
	 * @param amount
	 * @param transactionDate
	 */
	private void validateTransaction(Long customerId, Double amount, Date transactionDate) {
		if (customerId == null) {
			throw new IllegalArgumentException("Customer ID cannot be null");
		}
		if (customerId < 0) {
			throw new IllegalArgumentException("Customer ID cannot be a negative value");
		}
		if (transactionDate == null) {
			throw new IllegalArgumentException("Transaction date cannot be null");
		}
		if (amount < 0) {
			throw new IllegalArgumentException("Amount cannot be less than zero");
		}

		try {
			LocalDate localDate = transactionDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			String formattedDate = localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			if (!formattedDate.equals(localDate.getYear() + "-" + String.format("%02d", localDate.getMonthValue()) + "-"
					+ String.format("%02d", localDate.getDayOfMonth()))) {
				throw new IllegalArgumentException("Transaction date must be in the format yyyy-MM-dd");
			}
		} catch (DateTimeException e) {
			throw new IllegalArgumentException("Invalid transaction date", e);
		}
	}
}
