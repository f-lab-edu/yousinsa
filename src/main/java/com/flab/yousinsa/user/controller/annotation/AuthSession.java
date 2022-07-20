package com.flab.yousinsa.user.controller.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 해당 API에 접근할 수 있는 정상적인 Session이 있는지 판단
 * - 로그인이 필요한 API에 Annotation 추가
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthSession {
}
