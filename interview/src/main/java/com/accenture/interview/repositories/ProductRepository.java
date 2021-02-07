package com.accenture.interview.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.accenture.interview.entities.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

}
