package com.flab.yousinsa.user.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class SignInFailException extends RuntimeException {
	public SignInFailException(String message) {
		super(message);
	}

	public SignInFailException(String message, Throwable cause) {
		super(message, cause);
	}

	public SignInFailException(Throwable cause) {
		super(cause);
	}
}
