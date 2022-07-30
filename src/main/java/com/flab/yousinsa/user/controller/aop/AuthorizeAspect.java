package com.flab.yousinsa.user.controller.aop;

import static com.flab.yousinsa.global.config.AopAspectConfig.*;
import static com.flab.yousinsa.user.controller.aop.AuthenticateAspect.*;

import java.lang.reflect.Method;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.flab.yousinsa.user.controller.annotation.RolePermission;
import com.flab.yousinsa.user.domain.dtos.AuthUser;
import com.flab.yousinsa.user.domain.enums.UserRole;
import com.flab.yousinsa.user.service.exception.AuthorizationException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Aspect
@Component
@Order(AUTHORIZE_ASPECT_ORDER)
public class AuthorizeAspect {

	private final HttpServletRequest httpServletRequest;

	@Around("@annotation(com.flab.yousinsa.user.controller.annotation.RolePermission)")
	public Object authorizeByRole(final ProceedingJoinPoint pjp) throws Throwable {
		HttpSession session = httpServletRequest.getSession(false);
		AuthUser authUser = (AuthUser)session.getAttribute(AUTH_USER);

		MethodSignature signature = (MethodSignature)(pjp.getSignature());
		Method method = signature.getMethod();

		RolePermission permissionAnnotation = method.getAnnotation(RolePermission.class);
		UserRole[] permittedRoles = permissionAnnotation.permittedRoles();
		log.debug(Arrays.toString(permittedRoles));

		boolean isAuthorized = Arrays.stream(permittedRoles)
			.anyMatch(permittedRole -> permittedRole.equals(authUser.getUserRole()));
		log.debug(String.valueOf(isAuthorized));

		if (!isAuthorized) {
			log.debug("Requested Handler need " + Arrays.toString(permittedRoles));
			throw new AuthorizationException("Requested handler need designated roles");
		}

		return pjp.proceed();
	}
}
