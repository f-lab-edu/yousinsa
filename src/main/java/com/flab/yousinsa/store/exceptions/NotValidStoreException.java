package com.flab.yousinsa.store.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class NotValidStoreException extends RuntimeException {
	public NotValidStoreException(String message) {
		super(message);
	}

	public NotValidStoreException(Throwable cause) {
		super(cause);
	}

	public NotValidStoreException(String message, Throwable cause) {
		super(message, cause);
	}
}
