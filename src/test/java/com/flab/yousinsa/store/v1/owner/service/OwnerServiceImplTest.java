package com.flab.yousinsa.store.v1.owner.service;

import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
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
import com.flab.yousinsa.store.v1.owner.converter.OwnerDtoConverter;
import com.flab.yousinsa.store.v1.owner.dtos.OwnerDto;
import com.flab.yousinsa.user.domain.entities.UserEntity;
import com.flab.yousinsa.user.domain.enums.UserRole;
import com.flab.yousinsa.user.repository.contract.UserRepository;

@ActiveProfiles("local")
@ExtendWith(MockitoExtension.class)
class OwnerServiceImplTest {

	@Mock
	OwnerDtoConverter ownerDtoConverter;

	@Mock
	UserRepository userRepository;

	@Mock
	StoreRepository storeRepository;

	@InjectMocks
	OwnerServiceImpl ownerServiceImpl;

	UserEntity user;
	Store store;

	@BeforeEach
	public void setup() {
		user = new UserEntity("test","test@test.com","test", UserRole.BUYER);
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

		given(userRepository.save(any(UserEntity.class))).willReturn(user);

		// when
		Long result = ownerServiceImpl.entryStore(request, user);

		// then
		Assertions.assertEquals(result, 1L);
	}
}
