package com.accenture.interview;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.accenture.interview.entities.Client;
import com.accenture.interview.entities.Product;
import com.accenture.interview.repositories.ClientRepository;
import com.accenture.interview.repositories.ProductRepository;

@Component
public class Runner implements CommandLineRunner {

	@Autowired
	ProductRepository productRepository;

	@Autowired
	ClientRepository clientRepository;

	@Override
	public void run(String... args) throws Exception {
		String[] names = { "Product1", "Product2", "Product3", "Product4", "Product5" };
		int[] prices = { 3000, 2000, 6400, 100000, 3400 };
		for (int i = 0; i < names.length; i++) {
			productRepository.save(new Product(names[i], new BigDecimal(prices[i])));
		}

		clientRepository.save(new Client("1005206910", "Juan Manuel G", "Mz J # 177"));
	}
}
