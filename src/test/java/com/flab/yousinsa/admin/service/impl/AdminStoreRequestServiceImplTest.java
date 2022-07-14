package com.flab.yousinsa.admin.service.impl;

import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.flab.yousinsa.admin.domain.dtos.RequestStoreDtoRequest;
import com.flab.yousinsa.admin.domain.dtos.RequestStoreDtoResponse;
import com.flab.yousinsa.admin.service.converter.StoreRequestDtoConverter;
import com.flab.yousinsa.admin.service.exceptions.InvalidStoreStateException;
import com.flab.yousinsa.admin.service.exceptions.NoValidStoreExistException;
import com.flab.yousinsa.annotation.UnitTest;
import com.flab.yousinsa.store.domain.Store;
import com.flab.yousinsa.store.domain.StoreRepository;
import com.flab.yousinsa.store.enums.StoreStatus;
import com.flab.yousinsa.user.domain.entities.UserEntity;
import com.flab.yousinsa.user.domain.enums.UserRole;

@ExtendWith(MockitoExtension.class)
class AdminStoreRequestServiceImplTest {

	@Mock
	StoreRepository storeRepository;

	@Mock
	StoreRequestDtoConverter storeRequestDtoConverter;

	@InjectMocks
	AdminStoreRequestServiceImpl adminStoreRequestService;

	@UnitTest
	@Test
	@DisplayName("존재하지 않는 Store Id에 대해 Accept를 요청")
	public void acceptStoreRequestOnInvalidStoreId() {
		// given
		Long wrongStoreId = 0L;
		RequestStoreDtoRequest requestStoreDtoRequest = new RequestStoreDtoRequest(wrongStoreId);
		given(storeRepository.findById(anyLong())).willReturn(Optional.empty());
		given(storeRequestDtoConverter.convertStoreRequestToStoreId(any(RequestStoreDtoRequest.class))).willReturn(
			requestStoreDtoRequest.getStoreId());

		// when, given
		Assertions.assertThatThrownBy(() -> adminStoreRequestService.acceptStoreRequest(requestStoreDtoRequest))
			.isInstanceOf(NoValidStoreExistException.class)
			.hasMessageContaining("no valid store not found");
	}

	@UnitTest
	@Test
	@DisplayName("신청중이 아닌 다른 상태의 Store에 대해 Accept를 요청")
	public void acceptStoreRequestOnInvalidStoreStats() {
		// given
		Long validStoreId = 1L;
		RequestStoreDtoRequest requestStoreDtoRequest = new RequestStoreDtoRequest(validStoreId);
		UserEntity user = new UserEntity("rejectedUser", "owner@yousinsa.com", "password", UserRole.STORE_OWNER);
		Store store = new Store(1L, "rejectedShop", user, StoreStatus.REJECTED);

		given(storeRepository.findById(anyLong())).willReturn(Optional.of(store));
		given(storeRequestDtoConverter.convertStoreRequestToStoreId(any(RequestStoreDtoRequest.class))).willReturn(
			requestStoreDtoRequest.getStoreId());

		// when, given
		Assertions.assertThatThrownBy(() -> adminStoreRequestService.acceptStoreRequest(requestStoreDtoRequest))
			.isInstanceOf(InvalidStoreStateException.class)
			.hasMessageContaining("store status is not valid, only for requested");
	}

	@UnitTest
	@Test
	@DisplayName("권한이 있는 Admin이 올바른 Store에 대해 Accept를 요청")
	public void acceptStoreRequestWithValidStoreByAdmin() {
		// given
		Long validStoreId = 1L;
		RequestStoreDtoRequest validRequest = new RequestStoreDtoRequest(validStoreId);
		RequestStoreDtoResponse validResponse = new RequestStoreDtoResponse(validStoreId, StoreStatus.ACCEPTED);
		UserEntity user = new UserEntity("requestedUser", "owner@yousinsa.com", "password", UserRole.STORE_OWNER);
		Store store = new Store(1L, "requestedShop", user, StoreStatus.REQUESTED);

		given(storeRepository.findById(anyLong())).willReturn(Optional.of(store));
		given(storeRequestDtoConverter.convertStoreRequestToStoreId(any(RequestStoreDtoRequest.class))).willReturn(
			validRequest.getStoreId());
		given(storeRequestDtoConverter.convertEntityToResponse(any(Store.class))).willReturn(validResponse);

		// when
		RequestStoreDtoResponse response = adminStoreRequestService.acceptStoreRequest(validRequest);

		// then
		Assertions.assertThat(response.getStoreId()).isEqualTo(validStoreId);
		Assertions.assertThat(response.getStoreStatus()).isEqualTo(StoreStatus.ACCEPTED);

		then(storeRepository).should().findById(validStoreId);
		then(storeRequestDtoConverter).should().convertStoreRequestToStoreId(eq(validRequest));
		then(storeRequestDtoConverter).should().convertEntityToResponse(eq(store));
	}
}
