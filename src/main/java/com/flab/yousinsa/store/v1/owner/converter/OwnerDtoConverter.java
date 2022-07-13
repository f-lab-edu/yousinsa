package com.flab.yousinsa.store.v1.owner.converter;

import org.springframework.stereotype.Component;

import com.flab.yousinsa.store.domain.Store;
import com.flab.yousinsa.store.enums.StoreStatus;
import com.flab.yousinsa.store.v1.owner.dtos.OwnerDto;
import com.flab.yousinsa.user.domain.entities.UserEntity;

@Component
public class OwnerDtoConverter {

	public Store convertOwnerRequestToEntity(OwnerDto.Post request, UserEntity user) {
		return Store.builder()
			.storeName(request.getStoreName())
			.storeOwner(user)
			.storeStatus(StoreStatus.REQUESTED)
			.build();
	}
}
