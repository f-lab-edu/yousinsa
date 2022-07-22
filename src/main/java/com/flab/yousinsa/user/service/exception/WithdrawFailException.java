package com.flab.yousinsa.user.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class WithdrawFailException extends RuntimeException {
	public WithdrawFailException(String message) {
		super(message);
	}

	public WithdrawFailException(String message, Throwable cause) {
		super(message, cause);
	}

	public WithdrawFailException(Throwable cause) {
		super(cause);
	}
}
