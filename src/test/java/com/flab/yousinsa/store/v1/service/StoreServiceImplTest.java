package com.flab.yousinsa.store.v1.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.flab.yousinsa.store.domain.Store;
import com.flab.yousinsa.store.domain.StoreRepository;
import com.flab.yousinsa.store.enums.StoreStatus;
import com.flab.yousinsa.store.v1.converter.StoreDtoConverter;
import com.flab.yousinsa.store.v1.dtos.StoreDto;
import com.flab.yousinsa.user.domain.dtos.AuthUser;
import com.flab.yousinsa.user.domain.entities.UserEntity;
import com.flab.yousinsa.user.domain.enums.UserRole;
import com.flab.yousinsa.user.repository.contract.UserRepository;

@ExtendWith(MockitoExtension.class)
class StoreServiceImplTest {

	@Mock
	UserRepository userRepository;

	@Mock
	StoreDtoConverter storeDtoConverter;

	@Mock
	StoreRepository storeRepository;

	@InjectMocks
	StoreServiceImpl storeServiceImpl;

	UserEntity user;
	Store store;

	@BeforeEach
	public void setup() {
		user = new UserEntity("test","test@test.com","test", UserRole.BUYER);
		store = Store.builder().id(1L).storeName("store").storeOwner(user).storeStatus(StoreStatus.REQUESTED).build();
	}

	@Test
	@DisplayName("입점 신청")
	public void createStore() {
		// given
		StoreDto.Post request = new StoreDto.Post();
		request.setStoreName("store");

		given(userRepository.save(any(UserEntity.class))).willReturn(user);
		given(userRepository.findById(anyLong())).willReturn(Optional.ofNullable(user));
		given(storeDtoConverter.convertOwnerRequestToEntity(any(StoreDto.Post.class), any(UserEntity.class))).willReturn(store);
		given(storeRepository.save(any(Store.class))).willReturn(store);

		// when
		UserEntity userEntity = userRepository.save(user);
		AuthUser authUser = new AuthUser(1L, userEntity.getUserName(), userEntity.getUserEmail(), userEntity.getUserRole());

		Long result = storeServiceImpl.createStore(request, authUser);

		// then
		then(storeRepository).should().save(store);
		assertThat(result).isEqualTo(1L);
	}
}
