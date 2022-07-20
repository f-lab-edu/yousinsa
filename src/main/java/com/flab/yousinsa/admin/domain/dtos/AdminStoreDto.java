package com.flab.yousinsa.admin.domain.dtos;

import com.flab.yousinsa.store.enums.StoreStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AdminStoreDto {

	private Long storeId;

	private String storeName;

	private Long ownerId;

	private StoreStatus storeStatus;
}
