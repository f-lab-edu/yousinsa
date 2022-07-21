package com.flab.yousinsa.store.v1.service;

import com.flab.yousinsa.store.v1.dtos.StoreDto;
import com.flab.yousinsa.user.domain.dtos.AuthUser;

public interface StoreService {
	// 입점 신청
	Long createStore(StoreDto.Post request, AuthUser user);
}
