package com.flab.yousinsa.user.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 이미 로그아웃 한 경우 추가적으로 User가 할 행동은 RESET하는 것 뿐이기 때문에 RESET_CONTENT를 반환함으로써
 * 정상 로그아웃 시와 동일하게 처리
 */
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
