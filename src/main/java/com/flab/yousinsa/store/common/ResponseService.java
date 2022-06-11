package com.flab.yousinsa.store.common;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ResponseService {

	private final MessageSourceAccessor messageSourceAccessor;

	public ErrorResponse error(ResponseType type) {
		return error(type, null);
	}

	public ErrorResponse error(ResponseType responseType, String details) {
		return new ErrorResponse(
			messageSourceAccessor.getMessage(responseType.getCode(), LocaleContextHolder.getLocale()),
			messageSourceAccessor.getMessage(responseType.getMessage(), LocaleContextHolder.getLocale()),
			details
		);
	}

	public BaseResponse<?> base(ResponseType type) {
		return base(type, null);
	}

	public <T> BaseResponse<?> base(ResponseType type, T result) {
		return new BaseResponse<>(
			messageSourceAccessor.getMessage(type.getCode(), LocaleContextHolder.getLocale()),
			messageSourceAccessor.getMessage(type.getMessage(), LocaleContextHolder.getLocale()),
			result
		);
	}
}
