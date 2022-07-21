package com.flab.yousinsa.admin.service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class NotPermittedException extends RuntimeException {
	public NotPermittedException(String message) {
		super(message);
	}

	public NotPermittedException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotPermittedException(Throwable cause) {
		super(cause);
	}
}
