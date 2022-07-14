package com.flab.yousinsa.admin.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flab.yousinsa.admin.domain.dtos.AdminStoreDto;
import com.flab.yousinsa.admin.service.contract.AdminStoreService;
import com.flab.yousinsa.admin.service.converter.AdminStoreDtoConverter;
import com.flab.yousinsa.store.domain.StoreRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class AdminStoreServiceImpl implements AdminStoreService {

	private final StoreRepository storeRepository;
	private final AdminStoreDtoConverter storeDtoConverter;

	public AdminStoreServiceImpl(StoreRepository storeRepository, AdminStoreDtoConverter storeDtoConverter) {
		this.storeRepository = storeRepository;
		this.storeDtoConverter = storeDtoConverter;
	}

	@Override
	public Page<AdminStoreDto> getAllStores(Pageable pageRequest) {
		return storeRepository.findAll(pageRequest)
			.map(store -> storeDtoConverter.convertStoreEntityToDto(store, store.getStoreOwner()));
	}
}
