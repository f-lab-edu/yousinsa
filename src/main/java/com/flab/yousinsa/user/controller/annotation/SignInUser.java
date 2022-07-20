package com.flab.yousinsa.user.controller.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 로그인 된 User정보가 필요한 경우 해당 Parameter에 Annotation 추가
 * - 해당 Parameter Type은 AuthUser.class
 * @see AuthSession 는 Authentication 여부만 판단함
 * @code
 * <pre>{
 * @AuthSession
 * @DeleteMapping("api/v1/users/{userId})
 * public ResponseEntity<Void> withDrawUser(@SignInUser AuthUser user, Long userId) {
 *
 * }
 * }</pre>
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface SignInUser {
}
