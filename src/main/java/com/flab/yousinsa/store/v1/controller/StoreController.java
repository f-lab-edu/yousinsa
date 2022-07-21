package com.flab.yousinsa.store.v1.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flab.yousinsa.store.v1.dtos.StoreDto;
import com.flab.yousinsa.store.v1.service.StoreService;
import com.flab.yousinsa.user.controller.annotation.AuthSession;
import com.flab.yousinsa.user.controller.annotation.SignInUser;
import com.flab.yousinsa.user.domain.dtos.AuthUser;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class StoreController {

	private final StoreService ownerService;

	/**
	 * 입점 신청
	 * [POST] /stores
	 *
	 * @param request 입점 신청시 필요한 데이터
	 * @param user 세션에 담긴 user 데이터
	 * @return 입점 신청 결과
	 */
	@AuthSession
	@PostMapping("/stores")
	public ResponseEntity<?> createStore(
		@RequestBody StoreDto.Post request,
		@SignInUser AuthUser user
	) {
		Long id = ownerService.createStore(request, user);
		return ResponseEntity.created(URI.create("/api/v1/stores/" + id)).build();
	}
}
