package com.flab.yousinsa.store.v1.owner.service;

import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.flab.yousinsa.store.domain.StoreRepository;
import com.flab.yousinsa.store.v1.owner.converter.OwnerDtoConverter;
import com.flab.yousinsa.store.v1.owner.dtos.OwnerDto;
import com.flab.yousinsa.user.domain.entities.UserEntity;
import com.flab.yousinsa.user.domain.enums.UserRole;

@ExtendWith(SpringExtension.class)
@Import({OwnerDtoConverter.class, OwnerServiceImpl.class})
class OwnerServiceImplTest {

	@Autowired
	OwnerDtoConverter ownerDtoConverter;

	@Autowired
	OwnerServiceImpl ownerService;

	@MockBean
	StoreRepository storeRepository;

	UserEntity user;

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

		given(ownerService.entryStore(request, user)).willReturn(1L);

		// when
		Long result = ownerService.entryStore(request, user);

		// then
		Assertions.assertEquals(result, 1L);
	}
}
