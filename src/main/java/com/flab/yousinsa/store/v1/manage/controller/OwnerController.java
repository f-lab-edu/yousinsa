package com.flab.yousinsa.store.v1.manage.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.flab.yousinsa.store.v1.manage.dtos.OwnerDto;
import com.flab.yousinsa.store.v1.manage.service.OwnerService;
import com.flab.yousinsa.user.domain.entities.UserEntity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class OwnerController {

	private final OwnerService ownerService;

	/**
	 * 입점 신청
	 * [POST] /store
	 *
	 * @param request 입점 신청시 필요한 데이터
	 * @param user 세션에 담긴 user 데이터
	 * @return 입점 신청 결과
	 */
	@PostMapping("/store")
	public ResponseEntity<?> entryStore(
		@RequestBody OwnerDto.Post request,
		@SessionAttribute(name = "user") UserEntity user) {

		Long id = ownerService.entryStore(request, user);
		return ResponseEntity.created(URI.create("/api/v1/store/" + id)).build();
	}
}
