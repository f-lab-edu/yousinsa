package com.flab.yousinsa.admin.service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoValidStoreExistException extends RuntimeException {

	public NoValidStoreExistException(String message) {
		super(message);
	}

	public NoValidStoreExistException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoValidStoreExistException(Throwable cause) {
		super(cause);
	}
}
