package com.accenture.interview.exceptions;

@SuppressWarnings("serial")
public class ClientNotFoundException extends BaseException {

	private String errorCode;

	public ClientNotFoundException() {
		super();
	}

	public ClientNotFoundException(String message, String errorCode) {
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
