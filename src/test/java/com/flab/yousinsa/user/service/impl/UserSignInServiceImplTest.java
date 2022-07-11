package com.flab.yousinsa.user.service.impl;

import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.flab.yousinsa.annotation.UnitTest;
import com.flab.yousinsa.user.domain.dtos.request.SignInRequestDto;
import com.flab.yousinsa.user.domain.dtos.response.SignInResponseDto;
import com.flab.yousinsa.user.domain.entities.UserEntity;
import com.flab.yousinsa.user.domain.enums.UserRole;
import com.flab.yousinsa.user.repository.contract.UserRepository;
import com.flab.yousinsa.user.service.PasswordEncoder;
import com.flab.yousinsa.user.service.converter.SignInDtoConverter;
import com.flab.yousinsa.user.service.exception.SignInFailException;

@ExtendWith(MockitoExtension.class)
class UserSignInServiceImplTest {

	@Mock
	UserRepository userRepository;

	@Mock
	SignInDtoConverter signInDtoConverter;

	@Mock
	PasswordEncoder passwordEncoder;

	@InjectMocks
	UserSignInServiceImpl userSignInService;

	UserEntity user;
	String password = "password";
	String hashedPassword = "hashedPassword";
	SignInResponseDto signInResponseDto;

	@BeforeEach
	public void setUp() {
		user = new UserEntity("key", "rlfbd5142@gmail.com", hashedPassword, UserRole.BUYER);
		signInResponseDto = new SignInResponseDto(1L, user.getUserName(), user.getUserEmail(), user.getUserRole());
	}

	@UnitTest
	@Test
	@DisplayName("가입한 적이 없는 유저가 로그인하는 경우")
	public void testTrySignInUserIfNoUser() {
		// given
		String noExistUserEmail = "no-user@gmail.com";
		given(userRepository.findByUserEmail(anyString())).willReturn(Optional.empty());
		SignInRequestDto signInRequestDto = new SignInRequestDto(noExistUserEmail, password);

		// when
		Assertions.assertThatThrownBy(() -> {
				SignInResponseDto signInResponseDto = userSignInService.trySignInUser(signInRequestDto);
			})
			.isInstanceOf(SignInFailException.class)
			.hasMessageContaining("request email is not found");

		then(userRepository).should().findByUserEmail(noExistUserEmail);
	}

	@UnitTest
	@Test
	@DisplayName("가입한 유저가 비밀번호를 잘못 입력하고 로그인하는 경우")
	public void testTrySignInUserIfUserExistInvalidPassword() {
		// given
		SignInRequestDto signInRequestDto = new SignInRequestDto(user.getUserEmail(), password);
		given(userRepository.findByUserEmail(anyString())).willReturn(Optional.of(user));
		given(passwordEncoder.isMatched(anyString(), anyString())).willReturn(false);

		// when
		Assertions.assertThatThrownBy(() -> {
				SignInResponseDto signInResponseDto = userSignInService.trySignInUser(signInRequestDto);
			})
			.isInstanceOf(SignInFailException.class)
			.hasMessageContaining("password is not valid with email");

		// then
		then(userRepository).should().findByUserEmail(signInResponseDto.getUserEmail());
		then(passwordEncoder).should().isMatched(eq(password), eq(hashedPassword));
	}

	@UnitTest
	@Test
	@DisplayName("가입한 유저가 비밀번호를 정상적으로 입력하고 로그인하는 경우")
	public void testTrySignInUserIfUserExistValidPassword() {
		//given
		SignInRequestDto signInRequestDto = new SignInRequestDto(user.getUserEmail(), password);
		given(userRepository.findByUserEmail(anyString())).willReturn(Optional.of(user));
		given(signInDtoConverter.convertUserToSignInUserResponse(any(UserEntity.class))).willReturn(signInResponseDto);
		given(passwordEncoder.isMatched(anyString(), anyString())).willReturn(true);

		// when
		SignInResponseDto signInResponseDto = userSignInService.trySignInUser(signInRequestDto);

		// then
		Assertions.assertThat(signInResponseDto.getUserName()).isEqualTo(user.getUserName());
		Assertions.assertThat(signInResponseDto.getUserEmail()).isEqualTo(user.getUserEmail());

		then(userRepository).should().findByUserEmail(eq(signInRequestDto.getUserEmail()));
		then(signInDtoConverter).should().convertUserToSignInUserResponse(eq(user));
		then(passwordEncoder).should().isMatched(eq(password), eq(hashedPassword));
	}
}
