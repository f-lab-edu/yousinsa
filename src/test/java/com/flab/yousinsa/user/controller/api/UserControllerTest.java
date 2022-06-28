package com.flab.yousinsa.user.controller.api;

import static com.flab.yousinsa.ApiDocumentUtils.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
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

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
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
	@DisplayName("회원가입 API Doc")
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

	@UnitTest
	@Test
	@DisplayName("로그인 API Doc")
	public void signIn() throws Exception {
		// given
		SignInRequestDto signInRequestDto = new SignInRequestDto(user.getUserEmail(), rawPassword);
		SignInResponseDto signInResponseDto = new SignInResponseDto(1L, user.getUserName(), user.getUserEmail(),
			user.getUserRole());

		MockHttpSession mockHttpSession = new MockHttpSession();

		given(userSignInService.trySignInUser(any(SignInRequestDto.class)))
			.willReturn(signInResponseDto);

		// when
		ResultActions resultActions = mockMvc.perform(post("/api/v1/users/sign-in").session(mockHttpSession)
			.content(objectMapper.writeValueAsString(signInRequestDto))
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
		);

		// then
		resultActions.andExpect(status().isOk())
			.andExpect(content().string(objectMapper.writeValueAsString(signInResponseDto)))
			.andDo(
				document("user-signin",
					getDocumentRequest(),
					getDocumentResponse(),
					requestFields(
						fieldWithPath("userEmail").type(JsonFieldType.STRING).description("로그인 하고자 하는 유저 이메일"),
						fieldWithPath("userPassword").type(JsonFieldType.STRING).description("로그인 하고자 하는 유저 비밀번호")
					),
					responseFields(
						fieldWithPath("id").type(JsonFieldType.NUMBER).description("로그인된 유저 아이디(pk)"),
						fieldWithPath("userName").type(JsonFieldType.STRING).description("로그인된 유저 이름"),
						fieldWithPath("userEmail").type(JsonFieldType.STRING).description("로그인된 유저 이메일"),
						fieldWithPath("userRole").type(JsonFieldType.STRING).description("로그인된 유저 역할")
					)
				)
			);

		AuthUser authUser = (AuthUser)mockHttpSession.getAttribute(AuthWebConfig.Session.AUTH_USER);
		assertThat(authUser.getUserEmail()).isEqualTo(signInResponseDto.getUserEmail());
		assertThat(authUser.getUserName()).isEqualTo(signInResponseDto.getUserName());
		assertThat(authUser.getUserRole()).isEqualTo(signInResponseDto.getUserRole());

		then(userSignInService).should().trySignInUser(refEq(signInRequestDto));
	}

	@UnitTest
	@Test
	@DisplayName("로그아웃 API Doc")
	public void signOut() throws Exception {
		// given
		MockHttpSession mockHttpSession = new MockHttpSession();
		SignInResponseDto signInResponseDto = new SignInResponseDto(1L, user.getUserName(), user.getUserEmail(),
			user.getUserRole());
		mockHttpSession.setAttribute(AuthWebConfig.Session.AUTH_USER, signInResponseDto);

		// when
		ResultActions resultActions = mockMvc.perform(delete("/api/v1/users/sign-out").session(mockHttpSession));

		// then
		resultActions.andExpect(status().isResetContent())
			.andDo(
				document("user-signout",
					getDocumentRequest(),
					getDocumentResponse())
			);

		assertThatThrownBy(() -> mockHttpSession.getAttribute(AuthWebConfig.Session.AUTH_USER))
			.isInstanceOf(IllegalStateException.class);
	}

	@UnitTest
	@Test
	@DisplayName("회원탈퇴 API Doc")
	public void withDraw() throws Exception {
		// given
		MockHttpSession mockHttpSession = new MockHttpSession();
		Long deleteTargetUserId = 1L;
		AuthUser authUser = new AuthUser(deleteTargetUserId, user.getUserName(), user.getUserEmail(),
			user.getUserRole());
		mockHttpSession.setAttribute(AuthWebConfig.Session.AUTH_USER, authUser);
		willDoNothing().given(userSignUpService).tryWithdrawUser(any(AuthUser.class), anyLong());

		// when
		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.delete("/api/v1/users/{userId}", deleteTargetUserId).session(
				mockHttpSession));

		// then
		resultActions.andExpect(status().isNoContent())
			.andDo(
				document("user-withdraw",
					getDocumentRequest(),
					getDocumentResponse(),
					pathParameters(
						parameterWithName("userId").description("userId of withdraw requested user")
							.attributes(key("type").value(LONG))
					)
				)
			);

		then(userSignUpService).should().tryWithdrawUser(refEq(authUser), refEq(deleteTargetUserId));
	}

	@UnitTest
	@Test
	@DisplayName("이미 로그아웃시 로그아웃 실패")
	public void signOutFail() throws Exception {
		// given
		// NoSession

		// when
		ResultActions resultActions = mockMvc.perform(delete("/api/v1/users/sign-out"));

		// then
		resultActions.andExpect(status().isUnauthorized());
	}

	/**
	 * 해당 되는 URL 패턴의 API가 있을 경우 Method가 다르면 401이 아니라 Method_Not_Allowed(405)가 반환됨
	 * 따라서 이 테스트가 변동 사항이 없도록 만들 예정이 없는 URL로 테스트를 진행
	 * @throws Exception
	 */
	@UnitTest
	@Test
	@DisplayName("회원가입, 로그인이 아닌 API에 대해서 Session이 없는 경우 인증되지 않음(401) 반환")
	public void authRejectByAuthUserInterceptor() throws Exception {
		// given
		// NoSession

		// when
		ResultActions resultActions = mockMvc.perform(get("/api/test"));

		// then
		resultActions.andExpect(status().isUnauthorized());
	}
}
