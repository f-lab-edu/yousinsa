package com.flab.yousinsa.user.controller.component;

import static com.flab.yousinsa.user.config.AuthWebConfig.Session.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.flab.yousinsa.user.controller.annotation.SessionAuth;
import com.flab.yousinsa.user.domain.dtos.AuthUser;

public class AuthUserHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		boolean hasSessionAuthAnnotation = parameter.hasMethodAnnotation(SessionAuth.class);
		boolean hasAuthUserType = AuthUser.class.isAssignableFrom(parameter.getParameterType());
		return hasSessionAuthAnnotation && hasAuthUserType;
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
		NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

		HttpServletRequest httpServletRequest = (HttpServletRequest)webRequest.getNativeRequest();
		HttpSession session = httpServletRequest.getSession(false);
		if (session == null) {
			return null;
		}

		return session.getAttribute(AUTH_USER);
	}
}
