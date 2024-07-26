/**
 * This is the main class use for running the application.
 * 
 */
package com.example.java_assignment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Main application class.
 * @author avinash.menon
 */
@SpringBootApplication
@EnableAsync
public class JavaAssignmentApplication {

	public static void main(String[] args) {
		SpringApplication.run(JavaAssignmentApplication.class, args);
	}

}
