package com.flab.yousinsa.user.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class SignUpFailException extends RuntimeException {
	public SignUpFailException(String message) {
		super(message);
	}

	public SignUpFailException(Throwable cause) {
		super(cause);
	}

	public SignUpFailException(String message, Throwable cause) {
		super(message, cause);
	}
}
