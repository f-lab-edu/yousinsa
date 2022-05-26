package com.flab.yousinsa.user.service.exception;

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
