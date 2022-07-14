package com.flab.yousinsa.admin.controller.api;

import static com.flab.yousinsa.ApiDocumentUtils.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.yousinsa.admin.domain.dtos.RequestStoreDtoRequest;
import com.flab.yousinsa.admin.domain.dtos.RequestStoreDtoResponse;
import com.flab.yousinsa.admin.service.contract.AdminStoreRequestService;
import com.flab.yousinsa.admin.service.exceptions.NotPermittedException;
import com.flab.yousinsa.annotation.UnitTest;
import com.flab.yousinsa.store.enums.StoreStatus;
import com.flab.yousinsa.user.config.AuthWebConfig;
import com.flab.yousinsa.user.domain.dtos.AuthUser;
import com.flab.yousinsa.user.domain.enums.UserRole;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@WebMvcTest(AdminRequestStoreController.class)
@AutoConfigureRestDocs
@MockBean(JpaMetamodelMappingContext.class)
class AdminRequestStoreControllerTest {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@MockBean
	AdminStoreRequestService adminStoreRequestService;

	AuthUser admin;
	AuthUser notAdmin;

	@BeforeEach
	public void setUp() {
		admin = new AuthUser(1L, "admin", "admin@yousinsa.com", UserRole.ADMIN);
		notAdmin = new AuthUser(2L, "notAdmin", "notAdmin@yousinsa.com", UserRole.STORE_OWNER);
	}

	@UnitTest
	@Test
	@DisplayName("[Admin]상점 신청 Accept API Doc")
	public void acceptAdminStoreRequest() throws Exception {
		// given
		long storeId = 1L;
		RequestStoreDtoRequest requestStoreDtoRequest = new RequestStoreDtoRequest(storeId);
		RequestStoreDtoResponse requestStoreDtoResponse = new RequestStoreDtoResponse(storeId, StoreStatus.ACCEPTED);

		MockHttpSession mockHttpSession = new MockHttpSession();
		mockHttpSession.setAttribute(AuthWebConfig.Session.AUTH_USER, admin);

		given(adminStoreRequestService.acceptStoreRequest(any(RequestStoreDtoRequest.class)))
			.willReturn(requestStoreDtoResponse);

		// when
		ResultActions resultActions = mockMvc.perform(
			put("/api/admin/v1/store").session(mockHttpSession)
				.content(objectMapper.writeValueAsString(requestStoreDtoRequest))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
		);

		// then
		resultActions.andExpect(status().isAccepted())
			.andExpect(content().string(objectMapper.writeValueAsString(requestStoreDtoResponse)))
			.andDo(
				document("admin-store-accept",
					getDocumentRequest(),
					getDocumentResponse(),
					requestFields(
						fieldWithPath("storeId").type(JsonFieldType.NUMBER).description("Accept를 진행할 Store ID")
					),
					responseFields(
						fieldWithPath("storeId").type(JsonFieldType.NUMBER).description("Accept가 진행된 Store ID"),
						fieldWithPath("storeStatus").type(JsonFieldType.STRING).description("Accepted Store의 상태")
					)
				)
			);

		then(adminStoreRequestService).should().acceptStoreRequest(refEq(requestStoreDtoRequest));
	}

	@UnitTest
	@Test
	@DisplayName("[Admin]Admin Role이 없는 User는 입점 신청 동의를 진행할 수 없음")
	public void acceptStoreRequestOnlyByAdmin() throws Exception {
		// given
		long storeId = 1L;
		RequestStoreDtoRequest requestStoreDtoRequest = new RequestStoreDtoRequest(storeId);
		RequestStoreDtoResponse requestStoreDtoResponse = new RequestStoreDtoResponse(storeId, StoreStatus.ACCEPTED);

		MockHttpSession mockHttpSession = new MockHttpSession();
		mockHttpSession.setAttribute(AuthWebConfig.Session.AUTH_USER, notAdmin);

		given(adminStoreRequestService.acceptStoreRequest(any(RequestStoreDtoRequest.class)))
			.willReturn(requestStoreDtoResponse);

		// when
		ResultActions resultActions = mockMvc.perform(
			put("/api/admin/v1/store").session(mockHttpSession)
				.content(objectMapper.writeValueAsString(requestStoreDtoRequest))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
		);

		// then
		resultActions.andExpect(status().isForbidden())
			.andExpect((result) -> Assertions.assertThat(result.getResolvedException())
				.isInstanceOf(NotPermittedException.class)
				.hasMessageContaining("store request can be accepted by admin")
			);
	}
}
