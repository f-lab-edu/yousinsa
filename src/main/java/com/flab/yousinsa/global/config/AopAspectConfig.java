package com.flab.yousinsa.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy
@Configuration
public class AopAspectConfig {
	public static final int AUTHENTICATE_ASPECT_ORDER = -1;
	public static final int AUTHORIZE_ASPECT_ORDER = AUTHENTICATE_ASPECT_ORDER + 1;
}
