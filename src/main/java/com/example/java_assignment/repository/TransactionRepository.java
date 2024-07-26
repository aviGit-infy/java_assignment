/**
 * This is the repository class which is extending the JPARepository and contains the method to find 
 * the customer by Id
 */
package com.example.java_assignment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.java_assignment.entity.Transactions;

/**
 * @author avinash.menon
 *
 */
public interface TransactionRepository extends JpaRepository<Transactions, Long> {

	/**
	 * find customers by Id
	 * 
	 * @param customerId
	 * @return
	 */
	List<Transactions> findByCustomerId(Long customerId);

}
