package com.flab.yousinsa.admin.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.flab.yousinsa.admin.domain.dtos.RequestStoreDtoResponse;
import com.flab.yousinsa.admin.service.contract.AdminStoreRequestService;
import com.flab.yousinsa.admin.service.converter.StoreRequestDtoConverter;
import com.flab.yousinsa.admin.service.exceptions.InvalidStoreStateException;
import com.flab.yousinsa.admin.service.exceptions.NoValidStoreExistException;
import com.flab.yousinsa.store.domain.Store;
import com.flab.yousinsa.store.domain.StoreRepository;
import com.flab.yousinsa.store.enums.StoreStatus;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AdminStoreRequestServiceImpl implements AdminStoreRequestService {

	private final StoreRepository storeRepository;
	private final StoreRequestDtoConverter storeRequestDtoConverter;

	public AdminStoreRequestServiceImpl(StoreRepository storeRepository,
		StoreRequestDtoConverter storeRequestDtoConverter) {
		this.storeRepository = storeRepository;
		this.storeRequestDtoConverter = storeRequestDtoConverter;
	}

	@Transactional
	@Override
	public RequestStoreDtoResponse acceptStoreRequest(Long storeId) {
		Assert.notNull(storeId, "storeId must be not null");

		Store requestedStore = storeRepository.findById(storeId).orElseThrow(() -> {
			log.debug("store id" + storeId + "does not exist");
			throw new NoValidStoreExistException("no valid store not found");
		});

		if (requestedStore.getStoreStatus() != StoreStatus.REQUESTED) {
			throw new InvalidStoreStateException("store status is not valid, only for requested");
		}

		requestedStore.setStoreStatus(StoreStatus.ACCEPTED);

		return storeRequestDtoConverter.convertEntityToResponse(requestedStore);
	}
}
