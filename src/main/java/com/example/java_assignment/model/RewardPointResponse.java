/**
 * This class is used to handle the response which is coming in the getRequest
 */
package com.example.java_assignment.model;

import java.util.Map;

/**
 * The RewardPointResponse class
 * 
 * @author avinash.menon
 *
 */
public class RewardPointResponse {

	/** The monthlyPoints*/
	private Map<String, Integer> monthlyPoints;
	
	/** The totalPoints */
	private int totalPoints;

	/**
	 * @param monthlyPoints
	 * @param totalPoints
	 */
	public RewardPointResponse(Map<String, Integer> monthlyPoints, int totalPoints) {
		super();
		this.monthlyPoints = monthlyPoints;
		this.totalPoints = totalPoints;
	}

	/**
	 * @return the monthlyPoints
	 */
	public Map<String, Integer> getMonthlyPoints() {
		return monthlyPoints;
	}

	/**
	 * @param monthlyPoints the monthlyPoints to set
	 */
	public void setMonthlyPoints(Map<String, Integer> monthlyPoints) {
		this.monthlyPoints = monthlyPoints;
	}

	/**
	 * @return the totalPoints
	 */
	public int getTotalPoints() {
		return totalPoints;
	}

	/**
	 * @param totalPoints the totalPoints to set
	 */
	public void setTotalPoints(int totalPoints) {
		this.totalPoints = totalPoints;
	}

}
