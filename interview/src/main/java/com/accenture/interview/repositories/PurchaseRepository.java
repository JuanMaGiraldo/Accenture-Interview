package com.accenture.interview.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.accenture.interview.entities.Purchase;
@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

}
