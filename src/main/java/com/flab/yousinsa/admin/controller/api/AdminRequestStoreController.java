package com.flab.yousinsa.admin.controller.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flab.yousinsa.admin.domain.dtos.RequestStoreDtoResponse;
import com.flab.yousinsa.admin.service.contract.AdminStoreRequestService;
import com.flab.yousinsa.admin.service.exceptions.NotPermittedException;
import com.flab.yousinsa.user.controller.annotation.SessionAuth;
import com.flab.yousinsa.user.domain.dtos.AuthUser;
import com.flab.yousinsa.user.domain.enums.UserRole;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class AdminRequestStoreController {

	private final AdminStoreRequestService storeRequestService;

	public AdminRequestStoreController(AdminStoreRequestService storeRequestService) {
		this.storeRequestService = storeRequestService;
	}

	@SessionAuth
	@PutMapping("api/admin/v1/stores/{storeId}")
	public ResponseEntity<RequestStoreDtoResponse> acceptStoreRequest(
		@PathVariable("storeId") Long storeId,
		AuthUser user
	) {
		isAuthUserAdmin(user);

		RequestStoreDtoResponse response = storeRequestService.acceptStoreRequest(storeId);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
	}

	private void isAuthUserAdmin(AuthUser user) {
		if (user.getUserRole() != UserRole.ADMIN) {
			throw new NotPermittedException("store request can be accepted by admin");
		}
	}
}
