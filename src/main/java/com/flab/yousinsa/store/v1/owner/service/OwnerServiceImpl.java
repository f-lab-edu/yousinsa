package com.flab.yousinsa.store.v1.owner.service;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.flab.yousinsa.store.domain.Store;
import com.flab.yousinsa.store.domain.StoreRepository;
import com.flab.yousinsa.store.exceptions.owner.NotValidOwnerException;
import com.flab.yousinsa.store.v1.owner.converter.OwnerDtoConverter;
import com.flab.yousinsa.store.v1.owner.dtos.OwnerDto;
import com.flab.yousinsa.user.domain.entities.UserEntity;

import lombok.RequiredArgsConstructor;

@Primary
@RequiredArgsConstructor
@Service
public class OwnerServiceImpl implements OwnerService {

	private final OwnerDtoConverter ownerDtoConverter;
	private final StoreRepository storeRepository;

	// 입점 신청
	@Override
	public Long entryStore(OwnerDto.Post request, UserEntity user) {
		validateOwner(user);
		Store entry = ownerDtoConverter.convertOwnerRequestToEntity(request, user);
		Store store = storeRepository.save(entry);
		return store.getId();
	}

	private void validateOwner(UserEntity user) {
		boolean isPresent = storeRepository.findByStoreOwner(user);
		if (isPresent) {
			throw new NotValidOwnerException("Already exists.");
		}
	}
}
