package com.flab.yousinsa.user.service.converter;

import org.springframework.stereotype.Component;

import com.flab.yousinsa.user.domain.dtos.request.SignUpRequestDto;
import com.flab.yousinsa.user.domain.dtos.response.SignUpResponseDto;
import com.flab.yousinsa.user.domain.entities.UserEntity;

@Component
public class SignUpDtoConverter {

	public UserEntity convertSignUpRequestToUser(SignUpRequestDto signUpRequest) {
		UserEntity signUpUser = new UserEntity(
			signUpRequest.getUserName(),
			signUpRequest.getUserEmail(),
			signUpRequest.getUserPassword(),
			signUpRequest.getUserRole()
		);

		return signUpUser;
	}

	public SignUpResponseDto convertUserToSignUpResponse(UserEntity user) {
		return new SignUpResponseDto(user.getId(), user.getUserName(), user.getUserEmail(), user.getUserRole());
	}
}
