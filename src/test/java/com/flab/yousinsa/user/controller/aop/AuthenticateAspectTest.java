package com.flab.yousinsa.user.controller.aop;

import static com.flab.yousinsa.user.controller.aop.AuthenticateAspect.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;

import com.flab.yousinsa.annotation.UnitTest;
import com.flab.yousinsa.user.controller.api.UserController;
import com.flab.yousinsa.user.domain.dtos.AuthUser;
import com.flab.yousinsa.user.domain.enums.UserRole;
import com.flab.yousinsa.user.service.contract.UserSignInService;
import com.flab.yousinsa.user.service.contract.UserSignUpService;
import com.flab.yousinsa.user.service.exception.AuthenticationException;

@ExtendWith(MockitoExtension.class)
@EnableAspectJAutoProxy(proxyTargetClass = true)
class AuthenticateAspectTest {

	private MockHttpSession mockHttpSession;
	private MockHttpServletRequest mockHttpServletRequest;

	private AuthUser admin;

	@Mock
	UserSignInService userSignInService;

	@Mock
	UserSignUpService userSignUpService;

	@BeforeEach
	public void setUp() {
		admin = new AuthUser(1L, "admin", "admin@yousinsa.com", UserRole.ADMIN);
		mockHttpServletRequest = new MockHttpServletRequest();
		mockHttpSession = new MockHttpSession();
	}

	@UnitTest
	@Test
	@DisplayName("유효한 Session일 경우 AuthenticateAspect AOP를 통과함")
	public void authenticateAspectTest() {
		// given
		UserController userController = new UserController(userSignUpService, userSignInService);
		mockHttpSession.setAttribute(AUTH_USER, admin);
		mockHttpServletRequest.setSession(mockHttpSession);

		AspectJProxyFactory factory = new AspectJProxyFactory(userController);
		AuthenticateAspect aspect = new AuthenticateAspect(mockHttpServletRequest);

		factory.addAspect(aspect);
		UserController proxy = factory.getProxy();

		// when
		ResponseEntity<Void> proxySignOutUser = proxy.signOutUser(mockHttpSession);

		// then
		Assertions.assertThat(proxySignOutUser.getStatusCode()).isEqualTo(HttpStatus.RESET_CONTENT);
	}

	@UnitTest
	@Test
	@DisplayName("Session이 없는 경우 AuthenticateAspect AOP를 통과하지 못함")
	public void authenticateAspectTestNoSession() {
		// given
		UserController userController = new UserController(userSignUpService, userSignInService);

		AspectJProxyFactory factory = new AspectJProxyFactory(userController);
		AuthenticateAspect aspect = new AuthenticateAspect(mockHttpServletRequest);

		factory.addAspect(aspect);
		UserController proxy = factory.getProxy();

		// when, then
		Assertions.assertThatThrownBy(() -> {
				proxy.signOutUser(mockHttpSession);
			})
			.isInstanceOf(AuthenticationException.class)
			.hasMessageContaining("Need to login for using this service");
	}
}


