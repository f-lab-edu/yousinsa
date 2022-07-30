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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;

import com.flab.yousinsa.admin.controller.api.AdminRequestStoreController;
import com.flab.yousinsa.admin.domain.dtos.RequestStoreDtoResponse;
import com.flab.yousinsa.admin.service.contract.AdminStoreRequestService;
import com.flab.yousinsa.annotation.UnitTest;
import com.flab.yousinsa.user.controller.config.SpringTestAopConfig;
import com.flab.yousinsa.user.domain.dtos.AuthUser;
import com.flab.yousinsa.user.domain.enums.UserRole;
import com.flab.yousinsa.user.service.exception.AuthorizationException;

@ContextConfiguration(classes = SpringTestAopConfig.class)
@ExtendWith(MockitoExtension.class)
class AuthorizeAspectTest {

	private MockHttpSession mockHttpSession;
	private MockHttpServletRequest mockHttpServletRequest;

	private AuthUser admin;
	private AuthUser notAdminOwner;
	private AuthUser notAdminBuyer;

	@Mock
	AdminStoreRequestService adminStoreRequestService;

	@BeforeEach
	public void setUp() {
		mockHttpSession = new MockHttpSession();
		mockHttpServletRequest = new MockHttpServletRequest();

		admin = new AuthUser(1L, "admin", "admin@yousinsa.com", UserRole.ADMIN);
		notAdminOwner = new AuthUser(2L, "notAdminOwner", "notAdminOwner@yousinsa.com", UserRole.STORE_OWNER);
		notAdminBuyer = new AuthUser(3L, "notAdminBuyer", "notAdminBuyer@yousinsa.com", UserRole.BUYER);
	}

	@UnitTest
	@Test
	@DisplayName("RolePermission에 명시된 Role을 가진 User만 해당 Aspect를 통과")
	public void authorizeByAop() {
		// given
		AdminRequestStoreController adminRequestStoreController = new AdminRequestStoreController(
			adminStoreRequestService);
		Long validStoreId = 1L;
		mockHttpSession.setAttribute(AUTH_USER, admin);
		mockHttpServletRequest.setSession(mockHttpSession);

		AspectJProxyFactory factory = new AspectJProxyFactory(adminRequestStoreController);
		AuthorizeAspect aspect = new AuthorizeAspect(mockHttpServletRequest);

		factory.addAspect(aspect);
		AdminRequestStoreController proxy = factory.getProxy();

		// when
		ResponseEntity<RequestStoreDtoResponse> proxyAcceptResponse = proxy.acceptStoreRequest(validStoreId);

		// then
		Assertions.assertThat(proxyAcceptResponse.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
	}

	@UnitTest
	@Test
	@DisplayName("RolePermission에 해당하지 않는 Role을 가진 User는 Aspect를 통과하지 못함(Owner)")
	public void authorizeByAopNotPermittedRoleOwner() {
		// given
		AdminRequestStoreController adminRequestStoreController = new AdminRequestStoreController(
			adminStoreRequestService);
		Long validStoreId = 1L;
		mockHttpSession.setAttribute(AUTH_USER, notAdminOwner);
		mockHttpServletRequest.setSession(mockHttpSession);

		AspectJProxyFactory factory = new AspectJProxyFactory(adminRequestStoreController);
		AuthorizeAspect aspect = new AuthorizeAspect(mockHttpServletRequest);

		factory.addAspect(aspect);
		AdminRequestStoreController proxy = factory.getProxy();

		// when, then
		Assertions.assertThatThrownBy(() -> {
				proxy.acceptStoreRequest(validStoreId);
			})
			.isInstanceOf(AuthorizationException.class)
			.hasMessageContaining("Requested handler need designated roles");
	}

	@UnitTest
	@Test
	@DisplayName("RolePermission에 해당하지 않는 Role을 가진 User는 Aspect를 통과하지 못함(Buyer)")
	public void authorizeByAopNotPermittedRoleBuyer() {
		// given
		AdminRequestStoreController adminRequestStoreController = new AdminRequestStoreController(
			adminStoreRequestService);
		Long validStoreId = 1L;
		mockHttpSession.setAttribute(AUTH_USER, notAdminBuyer);
		mockHttpServletRequest.setSession(mockHttpSession);

		AspectJProxyFactory factory = new AspectJProxyFactory(adminRequestStoreController);
		AuthorizeAspect aspect = new AuthorizeAspect(mockHttpServletRequest);

		factory.addAspect(aspect);
		AdminRequestStoreController proxy = factory.getProxy();

		// when, then
		Assertions.assertThatThrownBy(() -> {
				proxy.acceptStoreRequest(validStoreId);
			})
			.isInstanceOf(AuthorizationException.class)
			.hasMessageContaining("Requested handler need designated roles");
	}
}
