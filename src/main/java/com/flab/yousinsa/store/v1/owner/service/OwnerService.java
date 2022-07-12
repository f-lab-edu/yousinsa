package com.flab.yousinsa.store.v1.owner.service;

import com.flab.yousinsa.store.v1.owner.dtos.OwnerDto;
import com.flab.yousinsa.user.domain.entities.UserEntity;

public interface OwnerService {
	// 입점 신청
	Long entryStore(OwnerDto.Post request, UserEntity user);
}
