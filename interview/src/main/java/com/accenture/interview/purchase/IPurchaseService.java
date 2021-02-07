package com.accenture.interview.purchase;

import java.util.List;

import org.springframework.http.ResponseEntity;

public interface IPurchaseService {

	void deletePurchase(Long purchaseId);

	void updatePurchase(Long purchaseId, List<Long> newProductsId);

	void addPurchase(Long clientId, List<Long> productsId);

}
