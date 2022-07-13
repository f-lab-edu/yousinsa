package com.flab.yousinsa.store.v1.manage.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.flab.yousinsa.store.exceptions.manage.NotValidOwnerException;

@ControllerAdvice
public class OwnerControllerAdvice {

	@ExceptionHandler(NotValidOwnerException.class)
	public ResponseEntity<?> notValidOwnerException(NotValidOwnerException exception) {
		return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
	}
}
