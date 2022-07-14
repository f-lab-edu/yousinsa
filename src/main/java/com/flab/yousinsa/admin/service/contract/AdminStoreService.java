package com.flab.yousinsa.admin.service.contract;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.flab.yousinsa.admin.domain.dtos.AdminStoreDto;

public interface AdminStoreService {

	Page<AdminStoreDto> getAllStores(Pageable pageRequest);
}
