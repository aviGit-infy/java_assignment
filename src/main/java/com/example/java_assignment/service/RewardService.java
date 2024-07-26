/**
 * This is the interface used to call the implementation logic for the service class 
 */
package com.example.java_assignment.service;

/**
 * Interface for Reward Service. 
 * @author avinash.menon
 *
 */
public interface RewardService {

	/**
	 * contains the logic to calculate the reward points
	 * 
	 */
	public int calculateRewardPoints(Double amount);

}
