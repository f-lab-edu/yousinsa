package com.flab.yousinsa.user.controller.aop;

import static com.flab.yousinsa.global.config.AopAspectConfig.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.flab.yousinsa.user.service.exception.AuthenticationException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Aspect
@Component
@Order(AUTHENTICATE_ASPECT_ORDER)
public class AuthenticateAspect {

	public static final String AUTH_USER = "AuthUser";

	private final HttpServletRequest httpServletRequest;

	@Around("@annotation(com.flab.yousinsa.user.controller.annotation.AuthSession)")
	public Object accessSessionAuth(final ProceedingJoinPoint pjp) throws Throwable {
		HttpSession session = httpServletRequest.getSession(false);
		if (session == null) {
			throw new AuthenticationException("Need to login for using this service");
		}

		Object authUser = session.getAttribute(AUTH_USER);
		if (authUser == null) {
			throw new AuthenticationException("Valid session does not exists");
		}

		return pjp.proceed();
	}
}
