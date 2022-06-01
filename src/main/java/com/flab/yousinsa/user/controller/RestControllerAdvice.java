package com.flab.yousinsa.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.flab.yousinsa.user.service.exception.SignUpFailException;

@ControllerAdvice
public class RestControllerAdvice {

	@ExceptionHandler(SignUpFailException.class)
	public ResponseEntity<String> signUpFailed(Exception e) {

		// TODO ErrorResponse -> ResponseBody 추상화
		return ResponseEntity
			.status(HttpStatus.CONFLICT)
			.body("signUp Process Failed due to :: " + e.getMessage());
	}
}
