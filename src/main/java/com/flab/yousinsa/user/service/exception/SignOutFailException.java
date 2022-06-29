package com.flab.yousinsa.user.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.RESET_CONTENT)
public class SignOutFailException extends RuntimeException {
	public SignOutFailException(String message) {
		super(message);
	}

	public SignOutFailException(String message, Throwable cause) {
		super(message, cause);
	}

	public SignOutFailException(Throwable cause) {
		super(cause);
	}
}
