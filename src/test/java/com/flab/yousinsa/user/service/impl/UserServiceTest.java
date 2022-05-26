package com.flab.yousinsa.user.service.impl;

import com.flab.yousinsa.user.domain.dtos.request.SignUpRequestDto;
import com.flab.yousinsa.user.domain.dtos.response.SignUpResponseDto;
import com.flab.yousinsa.user.domain.entities.UserEntity;
import com.flab.yousinsa.user.domain.enums.UserRole;
import com.flab.yousinsa.user.repository.contract.UserRepository;
import com.flab.yousinsa.user.service.PasswordEncoder;
import com.flab.yousinsa.user.service.converter.SignUpDtoConverter;

import com.flab.yousinsa.user.service.exception.SignUpFailException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

	@Mock
	UserRepository userRepository;

	@Mock
	SignUpDtoConverter signUpDtoConverter;

	@Mock
	PasswordEncoder passwordEncoder;

	@Test
	@DisplayName("같은 이메일을 가진 유저가 없어 정상적으로 회원가입 되는 경우")
	public void signUpNoUserEmailTest() {
		// given
		UserEntity user = new UserEntity("key", "rlfbd5142@gmail.com", "hashedPassword", UserRole.BUYER);
		SignUpRequestDto signUpRequestDto = new SignUpRequestDto(user.getUserName(), user.getUserEmail(),
			"password", user.getUserRole());
		SignUpResponseDto signUpResponseDto = new SignUpResponseDto(1L, user.getUserName(), user.getUserEmail(),
			user.getUserRole());

		when(userRepository.save(any(UserEntity.class))).thenReturn(user);
		when(signUpDtoConverter.convertSignUpRequestToUser(any(SignUpRequestDto.class))).thenReturn(user);
		when(signUpDtoConverter.convertUserToSignUpResponse(any(UserEntity.class))).thenReturn(signUpResponseDto);
		when(passwordEncoder.hashPassword(signUpRequestDto.getUserPassword())).thenReturn("hashedPassword");

		UserService userService = new UserService(userRepository, signUpDtoConverter, passwordEncoder);

		// when
		SignUpResponseDto resultResponse = userService.trySignUpUser(signUpRequestDto);

		// then
		System.out.println("resultResponse.getUserName() = " + resultResponse.getUserName());
		System.out.println("resultResponse.getUserEmail() = " + resultResponse.getUserEmail());
		System.out.println("resultResponse.getUserRole() = " + resultResponse.getUserRole());
		assertThat(resultResponse.getUserName()).isEqualTo(signUpResponseDto.getUserName());
		assertThat(resultResponse.getUserEmail()).isEqualTo(signUpResponseDto.getUserEmail());
		assertThat(resultResponse.getUserRole()).isEqualTo(signUpResponseDto.getUserRole());
	}

	@Test
	@DisplayName("같은 이메일을 가진 유저가 있어 회원가입이 안되는 경우")
	public void signUpSameEmailUserExistTest() {
		// given
		UserEntity user = new UserEntity("key", "rlfbd5142@gmail.com", "hashedPassword", UserRole.BUYER);

		SignUpRequestDto signUpRequestDto = new SignUpRequestDto(user.getUserName(), user.getUserEmail(),
			"password", user.getUserRole());
		SignUpResponseDto signUpResponseDto = new SignUpResponseDto(1L, user.getUserName(), user.getUserEmail(),
			user.getUserRole());

		// 필요가 없는 Mock 객체는 필요없다고 Test 실패
		when(userRepository.findByUserEmail(any(String.class))).thenReturn(Optional.of(user));

		UserService userService = new UserService(userRepository, signUpDtoConverter, passwordEncoder);

		// when, then
		Assertions.assertThrows(SignUpFailException.class, () -> {
			SignUpResponseDto savedUser = userService.trySignUpUser(signUpRequestDto);
		});
	}
}