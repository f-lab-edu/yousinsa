package com.flab.yousinsa.user.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.flab.yousinsa.user.controller.component.AuthUserHandlerMethodArgumentResolver;
import com.flab.yousinsa.user.controller.interceptor.AuthUserInterceptor;

@Configuration
public class AuthWebConfig implements WebMvcConfigurer {

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new AuthUserInterceptor())
			.addPathPatterns("/**")
			.excludePathPatterns("/**/users/sign-up", "/**/users/sign-in");
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(new AuthUserHandlerMethodArgumentResolver());
	}

	public static class Session {
		public static final String AUTH_USER = "AuthUser";
	}
}
