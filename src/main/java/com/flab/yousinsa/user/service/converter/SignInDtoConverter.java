package com.flab.yousinsa.user.service.converter;

import org.springframework.stereotype.Component;

import com.flab.yousinsa.user.domain.dtos.response.SignInResponseDto;
import com.flab.yousinsa.user.domain.entities.UserEntity;

@Component
public class SignInDtoConverter {
	public SignInResponseDto convertUserToSignInUserResponse(UserEntity userEntity) {
		return new SignInResponseDto(
			userEntity.getId(),
			userEntity.getUserName(),
			userEntity.getUserEmail(),
			userEntity.getUserRole()
		);
	}
}
