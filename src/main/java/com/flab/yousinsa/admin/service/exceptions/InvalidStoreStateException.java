package com.flab.yousinsa.admin.service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidStoreStateException extends RuntimeException {
	public InvalidStoreStateException(String message) {
		super(message);
	}

	public InvalidStoreStateException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidStoreStateException(Throwable cause) {
		super(cause);
	}
}
