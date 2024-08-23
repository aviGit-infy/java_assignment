/**
 * This Test class is created to get the reward points for a Specific customer Id
 */
package com.example.java_assignment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.example.java_assignment.controller.RewardPointController;
import com.example.java_assignment.model.RewardPointResponse;
import com.example.java_assignment.service.AsyncRewardPointService;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;


/**
 * The RewardPointForSingleCustomerTest
 * @author avinash.menon
 *
 */
public class RewardPointForSingleCustomerTest {

	
	@Autowired
	private MockMvc mockMvc;

	@Mock
	private AsyncRewardPointService asyncRewardPointService;

	@InjectMocks
	private RewardPointController rewardPointController;
	
	/**
	 * This method is used to initialize the test environment and set up any
	 * necessary components required for the tests to run correctly.
	 */
	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(rewardPointController).build();
	}
	
	/**
	 * The below method is used to test the record for a specific customer is coming as correct 
	 * @throws Exception
	 */
	@Test
	public void testGetRewardPointsBasedOnCustomerId_Success() throws Exception {
		Long customerId = 1L;
		RewardPointResponse mockResponse = new RewardPointResponse(Map.of("jan", 100), 100);

		when(asyncRewardPointService.getRewardPointsAsync(customerId))
				.thenReturn(CompletableFuture.completedFuture(mockResponse));

		mockMvc.perform(get("/api/reward-points/1"))
				.andExpect(status().isOk()).andExpect(content().json("{\"monthlyPoints\":{\"jan\":100},\"totalPoints\":100}"));
	}

	/**
	 * The below test is for the input field validation
	 * @throws Exception
	 */
	@Test
	public void testGetRewardPointsBasedOnCustomerId_InvalidCustomerIdFormat() throws Exception {
		mockMvc.perform(get("/api/reward-points/abc")) // Invalid customer ID format
				.andExpect(status().isBadRequest()).andExpect(content().string("Invalid customer ID format"));
	}
	
	/**
	 * The below test is for negative input field validation
	 * @throws Exception
	 */
	@Test
	public void testGetRewardPointsBasedOnCustomerId_NegativeCustomerId() throws Exception {
		mockMvc.perform(get("/api/reward-points/-1")) // Negative customer ID
				.andExpect(status().isBadRequest())
				.andExpect(content().string("Customer ID must be a positive number"));
	}
	
}
