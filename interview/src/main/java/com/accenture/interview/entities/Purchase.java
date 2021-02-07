package com.accenture.interview.entities;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Purchase {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@CreationTimestamp
	private Date creationDate;

	private BigDecimal productsPrice;

	private BigDecimal penalty;

	private BigDecimal deliveryPrice;

	private BigDecimal fullPayment;

	private boolean purchaseActive;

	@ManyToMany(fetch = FetchType.LAZY)
	private List<Product> products = new ArrayList<>();

	@ManyToOne
	private Client client;

}
