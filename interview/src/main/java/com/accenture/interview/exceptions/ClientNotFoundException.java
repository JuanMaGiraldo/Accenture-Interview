package com.accenture.interview.exceptions;

public class ClientNotFoundException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1162547623215166770L;
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
