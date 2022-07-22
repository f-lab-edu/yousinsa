package com.flab.yousinsa.user.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.flab.yousinsa.user.controller.component.AuthUserHandlerMethodArgumentResolver;

@Configuration
public class AuthWebConfig implements WebMvcConfigurer {

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(new AuthUserHandlerMethodArgumentResolver());
	}
}
