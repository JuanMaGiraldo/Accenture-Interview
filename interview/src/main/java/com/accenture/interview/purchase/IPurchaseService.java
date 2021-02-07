package com.accenture.interview.purchase;

import java.util.List;

public interface IPurchaseService {

	void deletePurchase(Long purchaseId);

	void updatePurchase(Long purchaseId, List<Long> newProductsId);

	void addPurchase(Long clientId, List<Long> productsId);

}
