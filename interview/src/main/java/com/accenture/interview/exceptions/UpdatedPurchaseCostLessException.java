package com.accenture.interview.exceptions;

public class UpdatedPurchaseCostLessException extends BaseException {

	private String errorCode;

	public UpdatedPurchaseCostLessException() {
		super();
	}

	public UpdatedPurchaseCostLessException(String message, String errorCode) {
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
