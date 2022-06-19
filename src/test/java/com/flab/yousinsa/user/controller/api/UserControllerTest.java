package com.flab.yousinsa.user.controller.api;

import static com.flab.yousinsa.ApiDocumentUtils.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.yousinsa.user.domain.dtos.request.SignUpRequestDto;
import com.flab.yousinsa.user.domain.dtos.response.SignUpResponseDto;
import com.flab.yousinsa.user.domain.entities.UserEntity;
import com.flab.yousinsa.user.domain.enums.UserRole;
import com.flab.yousinsa.user.service.contract.UserSignUpService;

@ExtendWith({MockitoExtension.class, RestDocumentationExtension.class, SpringExtension.class})
@WebMvcTest(UserController.class)
@AutoConfigureRestDocs
@MockBean(JpaMetamodelMappingContext.class)
class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private UserSignUpService userSignUpService;

	private UserEntity user;

	@BeforeEach
	public void setUp() {
		user = new UserEntity("key", "rlfbd5142@gmail.com", "hashedPassword", UserRole.BUYER);
	}

	@Test
	@DisplayName("회원가입 API")
	public void signUp() throws Exception {
		// given
		SignUpRequestDto signUpRequestDto = new SignUpRequestDto(user.getUserName(), user.getUserEmail(),
			"password", user.getUserRole());
		SignUpResponseDto signUpResponseDto = new SignUpResponseDto(1L, user.getUserName(), user.getUserEmail(),
			user.getUserRole());

		given(userSignUpService.trySignUpUser(any(SignUpRequestDto.class)))
			.willReturn(signUpResponseDto);

		// when
		ResultActions result = mockMvc.perform(
			post("/api/v1/users")
				.content(objectMapper.writeValueAsString(signUpRequestDto))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
		);

		// then
		result.andExpect(status().isOk())
			.andExpect(content().string(objectMapper.writeValueAsString(signUpResponseDto)))
			.andDo(document("user-signup",
					getDocumentRequest(),
					getDocumentResponse(),
					requestFields(
						fieldWithPath("userName").type(JsonFieldType.STRING).description("이름"),
						fieldWithPath("userEmail").type(JsonFieldType.STRING).description("이메일"),
						fieldWithPath("userPassword").type(JsonFieldType.STRING).description("비밀번호"),
						fieldWithPath("userRole").type(JsonFieldType.STRING).description("역할")
					),
					responseFields(
						fieldWithPath("id").type(JsonFieldType.NUMBER).description("아이디(pk)"),
						fieldWithPath("userName").type(JsonFieldType.STRING).description("이름"),
						fieldWithPath("userEmail").type(JsonFieldType.STRING).description("이메일"),
						fieldWithPath("userRole").type(JsonFieldType.STRING).description("역할")
					)
				)
			);

		then(userSignUpService).should().trySignUpUser(refEq(signUpRequestDto));
	}
}
