package com.accenture.interview.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.accenture.interview.exceptions.BaseException;
import com.accenture.interview.exceptions.ClientNotFoundException;
import com.accenture.interview.purchase.IPurchaseService;

@RestController
@RequestMapping("/api/v1/purchase")
public class PurchaseController {
	@Autowired
	private IPurchaseService purchaseService;

	@PostMapping(path = "add")
	public ResponseEntity<String> addPurchase(@RequestParam("clientId") Long clientId,
			@RequestParam("productsId") List<Long> productsId) {
		try {
			this.purchaseService.addPurchase(clientId, productsId);
		} catch (BaseException e) {
			return new ResponseEntity<>(e.getErrorCode(), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>("The purchase was saved successfully.", HttpStatus.OK);
	}

	@PostMapping(path = "update")
	public ResponseEntity<String> updatePurchase(@RequestParam("purchaseId") Long purchaseId,
			@RequestParam("productsId") List<Long> productsId) {
		try {
			this.purchaseService.updatePurchase(purchaseId, productsId);
		} catch (BaseException e) {
			return new ResponseEntity<>(e.getErrorCode(), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>("The purchase was updated successfully.", HttpStatus.OK);
	}

	@PostMapping(path = "delete")
	public ResponseEntity<String> deletePurchase(@RequestParam("purchaseId") Long purchaseId) {
		try {
			this.purchaseService.deletePurchase(purchaseId);
		} catch (BaseException e) {
			return new ResponseEntity<>(e.getErrorCode(), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>("The purchase was deleted successfully.", HttpStatus.OK);
	}
}
