/**
 * The below class is created to Implement mock create Records for Transactions.
 */
package com.example.java_assignment;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.example.java_assignment.controller.RewardPointController;
import com.example.java_assignment.entity.Transactions;
import com.example.java_assignment.service.RewardServiceImplementation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * The CreateRewardPointForCustomerTest
 * @author avinash.menon
 *
 */
public class CreateRewardPointForCustomerTest {

	@Autowired
	private MockMvc mockMvc;

	@Mock
	private RewardServiceImplementation rewardServiceImplementation;

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
	 * The testCreateTransaction_Success method is a JUnit test case designed to
	 * verify the successful creation of a transaction records
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCreateTransactionSuccess() throws Exception {
		Transactions transaction = new Transactions();
		transaction.setCustomerId(1L);

		LocalDate localDate = LocalDate.parse("2024-08-12");
		transaction.setTransactionDate(localDate);
		transaction.setAmount(200.0);

		MvcResult result = mockMvc.perform(post("/api/reward-points/createTransactions")
				.contentType(MediaType.APPLICATION_JSON).content(asJsonString(transaction)))
				.andExpect(status().isOk())
				.andReturn();

		String response = result.getResponse().getContentAsString();
		assertEquals("Transaction created successfully", response);
	}

	/**
	 * The below function designed to convert a Java object into its JSON string
	 * representation using the Jackson library
	 * 
	 * @param obj
	 * @return
	 */
	private String asJsonString(Object obj) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.registerModule(new JavaTimeModule());
			return objectMapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

}
