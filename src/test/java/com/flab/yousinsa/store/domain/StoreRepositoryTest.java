package com.flab.yousinsa.store.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.flab.yousinsa.store.enums.StoreStatus;
import com.flab.yousinsa.user.domain.entities.UserEntity;
import com.flab.yousinsa.user.domain.enums.UserRole;

@DataJpaTest
class StoreRepositoryTest {

	@Autowired
	StoreRepository storeRepository;

	UserEntity user;

	@BeforeEach
	public void setUp() {
		user = new UserEntity("test","test@test.com","test", UserRole.BUYER);
	}

	@Test
	@DisplayName("입점 신청")
	public void createStore() {
		// given
		Store createStore = Store.builder()
			.storeName("store")
			.storeOwner(user)
			.storeStatus(StoreStatus.REQUESTED)
			.build();

		// when
		Store store = storeRepository.save(createStore);

		// then
		Assertions.assertEquals(createStore.getStoreName(), store.getStoreName());
		Assertions.assertEquals(createStore.getStoreOwner().getUserName(), store.getStoreOwner().getUserName());
	}

}
