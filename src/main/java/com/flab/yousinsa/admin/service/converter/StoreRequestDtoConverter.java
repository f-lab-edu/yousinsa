package com.flab.yousinsa.admin.service.converter;

import org.springframework.stereotype.Component;

import com.flab.yousinsa.admin.domain.dtos.RequestStoreDtoRequest;
import com.flab.yousinsa.admin.domain.dtos.RequestStoreDtoResponse;
import com.flab.yousinsa.store.domain.Store;

@Component
public class StoreRequestDtoConverter {

	public Long convertStoreRequestToStoreId(RequestStoreDtoRequest requestStoreDtoRequest) {
		return requestStoreDtoRequest.getStoreId();
	}

	public RequestStoreDtoResponse convertEntityToResponse(Store storeEntity) {
		return new RequestStoreDtoResponse(storeEntity.getId(), storeEntity.getStoreStatus());
	}
}

