package com.flab.yousinsa.admin.service.converter;

import org.springframework.stereotype.Component;

import com.flab.yousinsa.admin.domain.dtos.AdminStoreDto;
import com.flab.yousinsa.store.domain.Store;
import com.flab.yousinsa.user.domain.entities.UserEntity;

@Component
public class AdminStoreDtoConverter {

	public AdminStoreDto convertStoreEntityToDto(Store store, UserEntity owner) {
		return new AdminStoreDto(store.getId(), store.getStoreName(), owner.getId(), store.getStoreStatus());
	}

	public Store convertDtoToStoreEntity(AdminStoreDto storeDto, UserEntity owner) {
		return new Store(storeDto.getStoreId(), storeDto.getStoreName(), owner, storeDto.getStoreStatus());
	}
}
