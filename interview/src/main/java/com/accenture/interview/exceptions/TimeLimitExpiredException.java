package com.accenture.interview.exceptions;

@SuppressWarnings("serial")
public class TimeLimitExpiredException extends BaseException {

	private String errorCode;

	public TimeLimitExpiredException() {
		super();
	}

	public TimeLimitExpiredException(String message, String errorCode) {
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
