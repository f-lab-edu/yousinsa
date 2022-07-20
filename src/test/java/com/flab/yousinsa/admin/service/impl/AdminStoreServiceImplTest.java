package com.flab.yousinsa.admin.service.impl;

import static org.mockito.BDDMockito.*;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.flab.yousinsa.admin.domain.dtos.AdminStoreDto;
import com.flab.yousinsa.admin.service.converter.AdminStoreDtoConverter;
import com.flab.yousinsa.annotation.UnitTest;
import com.flab.yousinsa.store.domain.Store;
import com.flab.yousinsa.store.domain.StoreRepository;
import com.flab.yousinsa.store.enums.StoreStatus;
import com.flab.yousinsa.user.domain.entities.UserEntity;
import com.flab.yousinsa.user.domain.enums.UserRole;

@ExtendWith(MockitoExtension.class)
class AdminStoreServiceImplTest {

	@Mock
	StoreRepository storeRepository;

	@Mock
	AdminStoreDtoConverter adminStoreDtoConverter;

	@InjectMocks
	AdminStoreServiceImpl adminStoreService;

	UserEntity firstStoreOwner;
	UserEntity secondStoreOwner;
	UserEntity thirdStoreOwner;

	Store firstStore;
	Store secondStore;
	Store thirdStore;

	AdminStoreDto firstStoreDto;
	AdminStoreDto secondStoreDto;
	AdminStoreDto thirdStoreDto;

	@BeforeEach
	public void setUp() {
		firstStoreOwner = new UserEntity("first", "first@yousinsa.com", "password", UserRole.STORE_OWNER);
		secondStoreOwner = new UserEntity("second", "second@yousinsa.com", "password", UserRole.STORE_OWNER);
		thirdStoreOwner = new UserEntity("third", "third@yousinsa.com", "password", UserRole.STORE_OWNER);
		firstStore = new Store(1L, "firstStore", firstStoreOwner, StoreStatus.ACCEPTED);
		secondStore = new Store(2L, "secondStore", secondStoreOwner, StoreStatus.REJECTED);
		thirdStore = new Store(3L, "thirdStore", thirdStoreOwner, StoreStatus.PENDING);
		firstStoreDto = new AdminStoreDto(1L, "firstStore", 1L, StoreStatus.ACCEPTED);
		secondStoreDto = new AdminStoreDto(2L, "secondStore", 2L, StoreStatus.REJECTED);
		thirdStoreDto = new AdminStoreDto(3L, "thirdStore", 3L, StoreStatus.REJECTED);
	}

	@UnitTest
	@Test
	@DisplayName("모든 Store 리스트를 Pagination")
	public void listStorePage() {
		// given
		Pageable pageRequest = PageRequest.of(5, 10, Sort.Direction.ASC, "id");
		List<AdminStoreDto> storeDtoList = List.of(firstStoreDto, secondStoreDto, thirdStoreDto);
		List<Store> storeEntityList = List.of(firstStore, secondStore, thirdStore);
		Page<AdminStoreDto> storeDtoPage = new PageImpl<>(storeDtoList, pageRequest, storeDtoList.size());
		Page<Store> storeEntityPage = new PageImpl<>(storeEntityList, pageRequest, storeEntityList.size());

		given(storeRepository.findAll(any(Pageable.class))).willReturn(storeEntityPage);
		given(adminStoreDtoConverter.convertStoreEntityToDto(firstStore, firstStoreOwner)).willReturn(firstStoreDto);
		given(adminStoreDtoConverter.convertStoreEntityToDto(secondStore, secondStoreOwner)).willReturn(secondStoreDto);
		given(adminStoreDtoConverter.convertStoreEntityToDto(thirdStore, thirdStoreOwner)).willReturn(thirdStoreDto);

		// when
		Page<AdminStoreDto> allStores = adminStoreService.getAllStores(pageRequest);

		// then
		Assertions.assertThat(allStores.getContent()).containsAll(storeDtoPage.getContent());
		Assertions.assertThat(allStores.getTotalElements()).isEqualTo(storeDtoPage.getTotalElements());
		Assertions.assertThat(allStores.getTotalPages()).isEqualTo(storeDtoPage.getTotalPages());

		then(storeRepository).should().findAll(eq(pageRequest));
		then(adminStoreDtoConverter).should().convertStoreEntityToDto(eq(firstStore), eq(firstStoreOwner));
		then(adminStoreDtoConverter).should().convertStoreEntityToDto(eq(secondStore), eq(secondStoreOwner));
		then(adminStoreDtoConverter).should().convertStoreEntityToDto(eq(thirdStore), eq(thirdStoreOwner));
	}
}
