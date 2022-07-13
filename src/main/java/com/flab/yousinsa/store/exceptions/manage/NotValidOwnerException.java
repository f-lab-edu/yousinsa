package com.flab.yousinsa.store.exceptions.manage;

public class NotValidOwnerException extends RuntimeException {
	public NotValidOwnerException(String message) {
		super(message);
	}

	public NotValidOwnerException(Throwable cause) {
		super(cause);
	}

	public NotValidOwnerException(String message, Throwable cause) {
		super(message, cause);
	}
}
