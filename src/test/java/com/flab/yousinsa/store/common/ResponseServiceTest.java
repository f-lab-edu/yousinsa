package com.flab.yousinsa.store.common;

import java.util.Locale;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ResponseServiceTest {

	@Autowired
	ResponseService responseService;

	@Test
	@DisplayName("기본 응답 (KR)")
	public void baseResponseKR() {
		Locale.setDefault(Locale.KOREA);
		BaseResponse<?> response = responseService.base(ResponseType.MESSAGE_TEST);
		Assertions.assertEquals("[Message] 테스트 코드", response.getCode());
		Assertions.assertEquals("[Message] 테스트 메시지", response.getMessage());
	}

	@Test
	@DisplayName("기본 응답 (US)")
	public void baseResponseUS() {
		Locale.setDefault(Locale.US);
		BaseResponse<?> response = responseService.base(ResponseType.MESSAGE_TEST);
		Assertions.assertEquals("[Message] Test code", response.getCode());
		Assertions.assertEquals("[Message] Test message", response.getMessage());
	}

	@Test
	@DisplayName("결과 응답 (US)")
	public void baseResponseWithResultUS() {
		Locale.setDefault(Locale.US);
		BaseResponse<?> response = responseService.base(ResponseType.MESSAGE_TEST, "result");
		Assertions.assertEquals("[Message] Test code", response.getCode());
		Assertions.assertEquals("[Message] Test message", response.getMessage());
		Assertions.assertEquals("result", response.getResult());
	}

	@Test
	@DisplayName("에러 응답 (KR)")
	public void errorResponseKR() {
		Locale.setDefault(Locale.KOREA);
		ErrorResponse response = responseService.error(ResponseType.EXCEPTION_TEST);
		Assertions.assertEquals("[Exception] 테스트 코드", response.getCode());
		Assertions.assertEquals("[Exception] 테스트 메시지", response.getMessage());
	}

	@Test
	@DisplayName("에러 응답 (US)")
	public void errorResponseUS() {
		Locale.setDefault(Locale.US);
		ErrorResponse response = responseService.error(ResponseType.EXCEPTION_TEST);
		Assertions.assertEquals("[Exception] Test code", response.getCode());
		Assertions.assertEquals("[Exception] Test message", response.getMessage());
	}

}
