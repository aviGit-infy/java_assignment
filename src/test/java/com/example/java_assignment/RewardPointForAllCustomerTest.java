/**
 * This test class is created to Implement the Reward Point for All the Customer
 */
package com.example.java_assignment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.example.java_assignment.controller.RewardPointController;
import com.example.java_assignment.model.RewardPointForAllCustomer;
import com.example.java_assignment.service.AsyncRewardPointService;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * The RewardPointForAllCustomerTest
 * @author avinash.menon
 *
 */
public class RewardPointForAllCustomerTest {

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
	 * The below method used to get all the records for the given input from transaction.
	 * @throws Exception
	 */
	@Test
	public void testGetAllRewardTransactionsSuccess() throws Exception {
		RewardPointForAllCustomer customer1 = new RewardPointForAllCustomer(1L, Map.of("jan", 100), 100);
		RewardPointForAllCustomer customer2 = new RewardPointForAllCustomer(2L, Map.of("feb", 200), 200);
		List<RewardPointForAllCustomer> mockResponse = Arrays.asList(customer1, customer2);

		when(asyncRewardPointService.getRecordsForAllCustomers())
				.thenReturn(CompletableFuture.completedFuture(mockResponse));

		String expectedResponse = new ObjectMapper().writeValueAsString(mockResponse);

		mockMvc.perform(get("/api/reward-points/getAllCustomer")).andExpect(status().isOk()).andExpect(result -> {
			String actualResponse = result.getResponse().getContentAsString();
			assertEquals(expectedResponse, actualResponse);
		});

		/*
		 * mockMvc.perform(get("/api/reward-points/getAllCustomer"))
		 * .andExpect(status().isOk()).andExpect(content().json(
		 * "[{\"customerId\":1,\"monthlyPoints\":{\"jan\":100},\"totalPoints\":100},
		 * {\"customerId\":2,\"monthlyPoints\":{\"feb\":200},\"totalPoints\":200}]"));
		 */
	}
	
}
