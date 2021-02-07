package com.accenture.interview.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.accenture.interview.entities.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long>{

}
