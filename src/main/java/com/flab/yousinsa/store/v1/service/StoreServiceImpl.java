package com.flab.yousinsa.store.v1.service;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.flab.yousinsa.store.domain.Store;
import com.flab.yousinsa.store.domain.StoreRepository;
import com.flab.yousinsa.store.exceptions.NotValidStoreException;
import com.flab.yousinsa.store.v1.converter.StoreDtoConverter;
import com.flab.yousinsa.store.v1.dtos.StoreDto;
import com.flab.yousinsa.user.domain.dtos.AuthUser;
import com.flab.yousinsa.user.domain.entities.UserEntity;
import com.flab.yousinsa.user.repository.contract.UserRepository;
import com.flab.yousinsa.user.service.exception.UserNotFoundException;

import lombok.RequiredArgsConstructor;

@Primary
@RequiredArgsConstructor
@Service
public class StoreServiceImpl implements StoreService {

	private final UserRepository userRepository;
	private final StoreDtoConverter storeDtoConverter;
	private final StoreRepository storeRepository;

	// 입점 신청
	@Override
	public Long createStore(StoreDto.Post request, AuthUser user) {

		UserEntity userEntity = validateStoreOwnerByUserId(user.getId());

		Store store = storeDtoConverter.convertOwnerRequestToEntity(request, userEntity);
		Store createdStore = storeRepository.save(store);
		return createdStore.getId();
	}

	private UserEntity validateStoreOwnerByUserId(Long userId) {
		UserEntity userEntity = userRepository.findById(userId)
			.orElseThrow(() -> new UserNotFoundException("User not found."));

		boolean isPresent = storeRepository.existsByStoreOwner(userEntity);
		if (isPresent) {
			throw new NotValidStoreException("Already exists.");
		}

		return userEntity;
	}
}
