package com.flab.yousinsa.user.controller.api;

import static com.flab.yousinsa.ApiDocumentUtils.*;
import static org.assertj.core.api.Assertions.*;
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
import org.springframework.mock.web.MockHttpSession;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.yousinsa.annotation.UnitTest;
import com.flab.yousinsa.user.config.AuthWebConfig;
import com.flab.yousinsa.user.domain.dtos.AuthUser;
import com.flab.yousinsa.user.domain.dtos.request.SignInRequestDto;
import com.flab.yousinsa.user.domain.dtos.request.SignUpRequestDto;
import com.flab.yousinsa.user.domain.dtos.response.SignInResponseDto;
import com.flab.yousinsa.user.domain.dtos.response.SignUpResponseDto;
import com.flab.yousinsa.user.domain.entities.UserEntity;
import com.flab.yousinsa.user.domain.enums.UserRole;
import com.flab.yousinsa.user.service.contract.UserSignInService;
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

	UserEntity user;
	String rawPassword = "password";
	@MockBean
	private UserSignInService userSignInService;

	@BeforeEach
	public void setUp() {
		user = new UserEntity("key", "rlfbd5142@gmail.com", "hashedPassword", UserRole.BUYER);
	}

	@UnitTest
	@Test
	@DisplayName("???????????? API Doc")
	public void signUp() throws Exception {
		// given
		SignUpRequestDto signUpRequestDto = new SignUpRequestDto(user.getUserName(), user.getUserEmail(),
			rawPassword, user.getUserRole());
		SignUpResponseDto signUpResponseDto = new SignUpResponseDto(1L, user.getUserName(), user.getUserEmail(),
			user.getUserRole());

		given(userSignUpService.trySignUpUser(any(SignUpRequestDto.class)))
			.willReturn(signUpResponseDto);

		// when
		ResultActions result = mockMvc.perform(
			post("/api/v1/users/sign-up")
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
						fieldWithPath("userName").type(JsonFieldType.STRING).description("??????"),
						fieldWithPath("userEmail").type(JsonFieldType.STRING).description("?????????"),
						fieldWithPath("userPassword").type(JsonFieldType.STRING).description("????????????"),
						fieldWithPath("userRole").type(JsonFieldType.STRING).description("??????")
					),
					responseFields(
						fieldWithPath("id").type(JsonFieldType.NUMBER).description("?????????(pk)"),
						fieldWithPath("userName").type(JsonFieldType.STRING).description("??????"),
						fieldWithPath("userEmail").type(JsonFieldType.STRING).description("?????????"),
						fieldWithPath("userRole").type(JsonFieldType.STRING).description("??????")
					)
				)
			);

		then(userSignUpService).should().trySignUpUser(refEq(signUpRequestDto));
	}

	@UnitTest
	@Test
	@DisplayName("????????? API Doc")
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
						fieldWithPath("userEmail").type(JsonFieldType.STRING).description("????????? ????????? ?????? ?????? ?????????"),
						fieldWithPath("userPassword").type(JsonFieldType.STRING).description("????????? ????????? ?????? ?????? ????????????")
					),
					responseFields(
						fieldWithPath("id").type(JsonFieldType.NUMBER).description("???????????? ?????? ?????????(pk)"),
						fieldWithPath("userName").type(JsonFieldType.STRING).description("???????????? ?????? ??????"),
						fieldWithPath("userEmail").type(JsonFieldType.STRING).description("???????????? ?????? ?????????"),
						fieldWithPath("userRole").type(JsonFieldType.STRING).description("???????????? ?????? ??????")
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
	@DisplayName("???????????? API Doc")
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
	@DisplayName("???????????? API Doc")
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
	@DisplayName("?????? ??????????????? ???????????? ??????")
	public void signOutFail() throws Exception {
		// given
		// NoSession

		// when
		ResultActions resultActions = mockMvc.perform(delete("/api/v1/users/sign-out"));

		// then
		resultActions.andExpect(status().isUnauthorized());
	}

	/**
	 * ?????? ?????? URL ????????? API??? ?????? ?????? Method??? ????????? 401??? ????????? Method_Not_Allowed(405)??? ?????????
	 * ????????? ??? ???????????? ?????? ????????? ????????? ?????? ????????? ?????? URL??? ???????????? ??????
	 * @throws Exception
	 */
	@UnitTest
	@Test
	@DisplayName("????????????, ???????????? ?????? API??? ????????? Session??? ?????? ?????? ???????????? ??????(401) ??????")
	public void authRejectByAuthUserInterceptor() throws Exception {
		// given
		// NoSession

		// when
		ResultActions resultActions = mockMvc.perform(get("/api/test"));

		// then
		resultActions.andExpect(status().isUnauthorized());
	}
}
