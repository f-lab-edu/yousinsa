package com.flab.yousinsa.global.config;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Locale;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.support.MessageSourceAccessor;

@SpringBootTest
class MessageConfigTest {

	@Autowired
	private MessageSourceAccessor messageSourceAccessor;

	@Test
	@DisplayName("getMessage() 동작 확인")
	public void getMessage() {
		assertEquals("[Message] 테스트", messageSourceAccessor.getMessage("message.test", Locale.KOREA));
		assertEquals("[Message] Test", messageSourceAccessor.getMessage("message.test", Locale.US));

		assertEquals("[Exception] 테스트", messageSourceAccessor.getMessage("exception.test", Locale.KOREA));
		assertEquals("[Exception] Test", messageSourceAccessor.getMessage("exception.test", Locale.US));
	}

}