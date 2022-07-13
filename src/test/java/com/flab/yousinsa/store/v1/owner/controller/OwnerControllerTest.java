package com.flab.yousinsa.store.v1.owner.controller;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.yousinsa.store.v1.owner.dtos.OwnerDto;
import com.flab.yousinsa.store.v1.owner.service.OwnerService;
import com.flab.yousinsa.user.domain.entities.UserEntity;
import com.flab.yousinsa.user.domain.enums.UserRole;

@ActiveProfiles("test")
@WebMvcTest(OwnerController.class)
class OwnerControllerTest {

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	MockMvc mockMvc;

	@MockBean
	OwnerService ownerService;

	MockHttpSession session;

	UserEntity user;

	@BeforeEach
	public void setup() {
		user = new UserEntity("test","test@test.com","test", UserRole.BUYER);
		session = new MockHttpSession();
		session.setAttribute("user", user);
	}

	@AfterEach
	public void cleanup() {
		session.clearAttributes();
	}

	@Test
	@DisplayName("입점 신청")
	public void storeEntry() throws Exception {
		// given
		OwnerDto.Post request = new OwnerDto.Post();
		request.setStoreName("store");

		given(ownerService.entryStore(request, user)).willReturn(1L);

		// when
		String json = objectMapper.writeValueAsString(request);

		mockMvc.perform(post("/api/v1/store")
				.contentType(MediaType.APPLICATION_JSON)
				.session(session)
				.content(json))
			.andExpect(status().isCreated())
			.andDo(print());

		// then
		verify(ownerService).entryStore(refEq(request), refEq(user));
	}
}
