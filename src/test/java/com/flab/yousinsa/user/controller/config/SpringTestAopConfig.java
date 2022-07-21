package com.flab.yousinsa.user.controller.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@TestConfiguration
@EnableAspectJAutoProxy
@ComponentScan(basePackages = {"com.flab.yousinsa.user.controller.aop"})
public class SpringTestAopConfig {
}
