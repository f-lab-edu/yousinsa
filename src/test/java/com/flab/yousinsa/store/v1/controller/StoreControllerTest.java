package com.flab.yousinsa.store.v1.controller;

import static com.flab.yousinsa.ApiDocumentUtils.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.yousinsa.store.v1.dtos.StoreDto;
import com.flab.yousinsa.store.v1.service.StoreService;
import com.flab.yousinsa.user.controller.aop.AuthenticateAspect;
import com.flab.yousinsa.user.domain.dtos.AuthUser;
import com.flab.yousinsa.user.domain.enums.UserRole;

@ExtendWith({RestDocumentationExtension.class})
@AutoConfigureRestDocs
@WebMvcTest(StoreController.class)
class StoreControllerTest {

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	MockMvc mockMvc;

	@MockBean
	StoreService storeService;

	MockHttpSession session;

	AuthUser authUser;

	@BeforeEach
	public void setup() {
		authUser = new AuthUser(1L, "test","test@test.com", UserRole.BUYER);
		session = new MockHttpSession();
		session.setAttribute(AuthenticateAspect.AUTH_USER, authUser);
	}

	@AfterEach
	public void cleanup() {
		session.clearAttributes();
	}

	@Test
	@DisplayName("입점 신청")
	public void createStore() throws Exception {
		// given
		StoreDto.Post request = new StoreDto.Post();
		request.setStoreName("store");

		given(storeService.createStore(any(StoreDto.Post.class), any(AuthUser.class))).willReturn(1L);

		// when
		String json = objectMapper.writeValueAsString(request);

		ResultActions result = mockMvc
			.perform(post("/api/v1/stores")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.session(session)
				.content(json)
			);

		result
			.andExpect(status().isCreated())
			.andExpect(header().exists(HttpHeaders.LOCATION))
			.andDo(
				document("create-store",
					getDocumentRequest(),
					getDocumentResponse(),
					requestFields(
						fieldWithPath("storeName").type(JsonFieldType.STRING).description("입점명")
					),
					responseHeaders(
						headerWithName(HttpHeaders.LOCATION).description("입점 신청 후 이동할 URI")
					)
				)
			);

		// then
		then(storeService).should().createStore(refEq(request), refEq(authUser));
	}
}
