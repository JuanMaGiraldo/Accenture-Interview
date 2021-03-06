package com.accenture.interview.exceptions;

@SuppressWarnings("serial")
public abstract class BaseException extends RuntimeException {

	public BaseException() {
		super();
	}

	public BaseException(String message) {
		super(message);
	}

	public abstract String getErrorCode();
}
