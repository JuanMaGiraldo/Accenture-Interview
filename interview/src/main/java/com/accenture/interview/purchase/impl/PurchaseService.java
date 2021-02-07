package com.accenture.interview.purchase.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.accenture.interview.entities.Client;
import com.accenture.interview.entities.Product;
import com.accenture.interview.entities.Purchase;
import com.accenture.interview.exceptions.ClientNotFoundException;
import com.accenture.interview.exceptions.PurchaseNotFoundException;
import com.accenture.interview.exceptions.TimeLimitExpiredException;
import com.accenture.interview.exceptions.UpdatedPurchaseCostLessException;
import com.accenture.interview.purchase.IPurchaseService;
import com.accenture.interview.repositories.ClientRepository;
import com.accenture.interview.repositories.ProductRepository;
import com.accenture.interview.repositories.PurchaseRepository;
import com.accenture.interview.util.ResponseCode;
import com.accenture.interview.util.SystemProperties;

@Service
public class PurchaseService implements IPurchaseService {

	@Autowired
	private PurchaseRepository purchaseRepository;
	@Autowired
	private ClientRepository clientRepository;
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private SystemProperties systemProperties;

	@Override
	public void addPurchase(Long clientId, List<Long> productsId) {

		Optional<Client> clientOptional = clientRepository.findById(clientId);

		if (!clientOptional.isPresent()) {
			throw new ClientNotFoundException("The client was not found", ResponseCode.ERROR_CLIENT_NOT_FOUND);
		}

		List<Product> listProducts = getProducts(productsId);

		Purchase purchase = initializePurchase(clientOptional.get(), listProducts);
		calculatePrices(purchase);

		purchaseRepository.save(purchase);
	}

	@Override
	public void updatePurchase(Long purchaseId, List<Long> newProductsId) {

		Optional<Purchase> purchaseOptional = this.purchaseRepository.findById(purchaseId);

		if (!purchaseOptional.isPresent()) {
			throw new PurchaseNotFoundException("The purchase was not found.", ResponseCode.ERROR_PURCHASE_NOT_FOUND);
		}

		Purchase purchase = purchaseOptional.get();

		if (!purchase.isPurchaseActive()) {
			throw new PurchaseNotFoundException("The purchase was not found.", ResponseCode.ERROR_PURCHASE_NOT_FOUND);
		}

		List<Product> newProducts = getProducts(newProductsId);

		if (timeLimitToUpdateHasPassed(purchase)) {
			throw new TimeLimitExpiredException("The time limit to update your order has expired.",
					ResponseCode.TIME_LIMIT_UPDATE_PURCHASE_EXPIRED);
		}

		if (newProductsCostLess(newProducts, purchase.getProductsPrice())) {
			throw new UpdatedPurchaseCostLessException("The new products cost less than the old ones.",
					ResponseCode.UPDATED_PURCHASE_COST_LESS);
		}

		purchase.setProducts(newProducts);
		calculatePrices(purchase);

		purchaseRepository.save(purchase);
	}

	@Override
	public void deletePurchase(Long purchaseId) {

		Optional<Purchase> purchaseOptional = this.purchaseRepository.findById(purchaseId);

		if (!purchaseOptional.isPresent()) {
			throw new PurchaseNotFoundException("The purchase was not found.", ResponseCode.ERROR_PURCHASE_NOT_FOUND);
		}

		Purchase purchase = purchaseOptional.get();

		if (!purchase.isPurchaseActive()) {
			throw new PurchaseNotFoundException("The purchase was not found.", ResponseCode.ERROR_PURCHASE_NOT_FOUND);
		}

		if (timeLimitToDeleteHasPassed(purchase)) {
			updatePenalty(purchase);
		}

		cancelPurchase(purchase);

		purchaseRepository.save(purchase);
	}

	public boolean newProductsCostLess(List<Product> products, BigDecimal oldTotal) {
		BigDecimal newPrice = getProductsPrice(products);

		return newPrice.compareTo(oldTotal) < 0;
	}

	public boolean timeLimitToUpdateHasPassed(Purchase purchase) {
		return timeLimiteHasPassed(systemProperties.getUpdateHourTimeLimit(), purchase);
	}

	public boolean timeLimitToDeleteHasPassed(Purchase purchase) {
		return timeLimiteHasPassed(systemProperties.getDeleteHourTimeLimit(), purchase);
	}

	public boolean timeLimiteHasPassed(float time_limit, Purchase purchase) {
		float secondsFromCreationDate = getSecondsFromCreationDate(purchase);
		float hoursFromCreationDate = secondsFromCreationDate / 3600;

		return hoursFromCreationDate > time_limit;
	}

	public BigDecimal getProductsPrice(List<Product> products) {
		BigDecimal total = new BigDecimal(0);

		for (Product product : products) {
			total = total.add(product.getPrice());
		}

		return total;
	}

	public void calculatePrices(Purchase purchase) {
		calculateProductsPrice(purchase);
		calculateDelivery(purchase);
		calculateTotal(purchase);
	}

	public void updatePenalty(Purchase purchase) {
		BigDecimal penalty = purchase.getFullPayment().multiply(new BigDecimal(0.1));
		purchase.setPenalty(penalty);
	}

	public void calculateProductsPrice(Purchase purchase) {
		BigDecimal productsPrice = new BigDecimal(0);

		for (Product product : purchase.getProducts()) {
			productsPrice = productsPrice.add(product.getPrice());
		}

		purchase.setProductsPrice(productsPrice);
	}

	private void calculateDelivery(Purchase purchase) {
		BigDecimal deliveryPrice = (purchase.getProductsPrice().compareTo(new BigDecimal(100000)) > 0
				? new BigDecimal(0)
				: systemProperties.getDeliveryPrice());

		purchase.setDeliveryPrice(deliveryPrice);
	}

	private void calculateTotal(Purchase purchase) {
		BigDecimal ivaMultiplier = new BigDecimal(100).add(systemProperties.getIvaPercentage())
				.divide(new BigDecimal(100));
		
		BigDecimal fullPayment = (purchase.getProductsPrice().multiply(ivaMultiplier).add(purchase.getDeliveryPrice()));
		purchase.setFullPayment(fullPayment);
	}

	public void cancelPurchase(Purchase purchase) {
		purchase.setPurchaseActive(false);
		purchase.setFullPayment(purchase.getPenalty());
	}

	private List<Product> getProducts(List<Long> productsId) {
		List<Product> products = new ArrayList<>();
		for (Long id : productsId) {
			Optional<Product> productOptional = productRepository.findById(id);
			if (productOptional.isPresent()) {
				products.add(productOptional.get());
			}
		}
		return products;
	}

	public float getSecondsFromCreationDate(Purchase purchase) {
		Date actualDate = new Date();
		long diffInMillies = Math.abs(actualDate.getTime() - purchase.getCreationDate().getTime());
		long secsDiff = TimeUnit.SECONDS.convert(diffInMillies, TimeUnit.MILLISECONDS);
		return secsDiff;
	}

	public Purchase initializePurchase(Client client, List<Product> listProducts) {
		Purchase purchase = new Purchase();
		purchase.setClient(client);
		purchase.setProducts(listProducts);
		purchase.setPenalty(new BigDecimal(0));
		purchase.setPurchaseActive(true);
		return purchase;
	}
}
