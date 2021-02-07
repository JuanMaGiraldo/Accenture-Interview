package com.accenture.interview;

import java.math.BigDecimal;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.accenture.interview.entities.Client;
import com.accenture.interview.entities.Product;
import com.accenture.interview.repositories.ClientRepository;
import com.accenture.interview.repositories.ProductRepository;

@SpringBootApplication
public class InterviewApplication {

	public static void main(String[] args) {
		SpringApplication.run(InterviewApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRuner(ProductRepository productRepository) {
		return args -> {
			String[] names = { "Product1", "Product2", "Product3", "Product4", "Product5" };
			int[] prices = { 3000, 2000, 6400, 100000, 3400 };
			for (int i = 0; i < names.length; i++) {
				productRepository.save(new Product(names[i], new BigDecimal(prices[i])));
			}
		};
	}

	@Bean
	CommandLineRunner commandLineRunner(ClientRepository clientRepository) {
		return args -> {
			clientRepository.save(new Client("1005206910", "Juan Manuel G", "Mz J # 177"));
		};
	}

}
