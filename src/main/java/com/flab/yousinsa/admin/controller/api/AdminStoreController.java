package com.flab.yousinsa.admin.controller.api;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flab.yousinsa.admin.domain.dtos.AdminStoreDto;
import com.flab.yousinsa.admin.service.contract.AdminStoreService;
import com.flab.yousinsa.admin.service.exceptions.NotPermittedException;
import com.flab.yousinsa.user.controller.annotation.AuthSession;
import com.flab.yousinsa.user.controller.annotation.SignInUser;
import com.flab.yousinsa.user.domain.dtos.AuthUser;
import com.flab.yousinsa.user.domain.enums.UserRole;

@RestController
public class AdminStoreController {

	private final AdminStoreService adminStoreService;

	public AdminStoreController(AdminStoreService adminStoreService) {
		this.adminStoreService = adminStoreService;
	}

	@AuthSession
	@GetMapping("/api/admin/v1/stores")
	public ResponseEntity<Page<AdminStoreDto>> listRegisteredStore(Pageable pageable, @SignInUser AuthUser user) {
		isAuthUserAdmin(user);

		Page<AdminStoreDto> allStores = adminStoreService.getAllStores(pageable);

		return ResponseEntity.ok(allStores);
	}

	private void isAuthUserAdmin(AuthUser user) {
		if (user.getUserRole() != UserRole.ADMIN) {
			throw new NotPermittedException("store list can be viewed by admin");
		}
	}
}
