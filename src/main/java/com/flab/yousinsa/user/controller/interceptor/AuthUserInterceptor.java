package com.flab.yousinsa.user.controller.interceptor;

import static com.flab.yousinsa.user.config.AuthWebConfig.Session.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.flab.yousinsa.user.service.exception.AuthException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AuthUserInterceptor implements HandlerInterceptor {

	/**
	 * Session을 통한 User Auth Check
	 * - 회원 가입, 로그인을 제외한 Handler에 필요한 Authenication 작업을 Interceptor로 처리
	 * - AOP 방식과 Interceptor 방식 고려
	 * - Interceptor 선정
	 * - Spring Security에서는 Filter를 통한 Authentication 지원
	 *
	 * @param request
	 * @param response
	 * @param handler
	 * @return
	 * @throws AuthException
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws
		Exception {

		HttpSession session = request.getSession(false);
		if (session == null) {
			log.debug("There is no session");
			throw new AuthException("Need to login for using this service");
		}

		Object authUser = session.getAttribute(AUTH_USER);

		return authUser != null;
	}
}
