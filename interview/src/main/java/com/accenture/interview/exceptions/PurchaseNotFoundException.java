package com.accenture.interview.exceptions;

public class PurchaseNotFoundException extends BaseException {

	private String errorCode;

	public PurchaseNotFoundException() {
		super();
	}

	public PurchaseNotFoundException(String message, String errorCode) {
		super(message);
		this.errorCode = errorCode;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

}
