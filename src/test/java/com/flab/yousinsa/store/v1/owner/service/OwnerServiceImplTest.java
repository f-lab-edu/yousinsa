package com.flab.yousinsa.store.v1.owner.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import com.flab.yousinsa.store.domain.Store;
import com.flab.yousinsa.store.domain.StoreRepository;
import com.flab.yousinsa.store.enums.StoreStatus;
import com.flab.yousinsa.store.v1.owner.converter.OwnerDtoConverter;
import com.flab.yousinsa.store.v1.owner.dtos.OwnerDto;
import com.flab.yousinsa.user.domain.entities.UserEntity;
import com.flab.yousinsa.user.domain.enums.UserRole;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class OwnerServiceImplTest {

	@Mock
	OwnerDtoConverter ownerDtoConverter;

	@Mock
	StoreRepository storeRepository;

	@InjectMocks
	OwnerServiceImpl ownerServiceImpl;

	UserEntity user;
	Store store;

	@BeforeEach
	public void setup() {
		user = new UserEntity("test","test@test.com","test", UserRole.BUYER);
		store = Store.builder().id(1L).storeName("store").storeOwner(user).storeStatus(StoreStatus.REQUESTED).build();
	}

	@AfterEach
	public void cleanup() {
		storeRepository.deleteAll();
	}

	@Test
	@DisplayName("입점 신청")
	public void storeEntry() {
		// given
		OwnerDto.Post request = new OwnerDto.Post();
		request.setStoreName("store");

		given(ownerDtoConverter.convertOwnerRequestToEntity(any(OwnerDto.Post.class), any(UserEntity.class))).willReturn(store);
		given(storeRepository.save(any(Store.class))).willReturn(store);

		// when
		Long result = ownerServiceImpl.entryStore(request, user);

		// then
		assertThat(result).isEqualTo(1L);
	}
}
