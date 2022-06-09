package com.flab.yousinsa.user.service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.flab.yousinsa.user.service.CustomCryptPasswordEncoder;
import com.flab.yousinsa.user.service.PasswordEncoder;

/**
 * 추후 Spring Security로 변경할 예정
 */
@Configuration
public class SecurityConfig {

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new CustomCryptPasswordEncoder();
	}
}
