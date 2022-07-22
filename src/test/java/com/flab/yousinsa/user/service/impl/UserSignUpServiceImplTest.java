package com.flab.yousinsa.user.service.impl;

import static org.assertj.core.api.Assertions.*;
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
import com.flab.yousinsa.user.domain.dtos.AuthUser;
import com.flab.yousinsa.user.domain.dtos.request.SignUpRequestDto;
import com.flab.yousinsa.user.domain.dtos.response.SignUpResponseDto;
import com.flab.yousinsa.user.domain.entities.UserEntity;
import com.flab.yousinsa.user.domain.enums.UserRole;
import com.flab.yousinsa.user.repository.contract.UserRepository;
import com.flab.yousinsa.user.service.PasswordEncoder;
import com.flab.yousinsa.user.service.converter.SignUpDtoConverter;
import com.flab.yousinsa.user.service.exception.AuthenticationException;
import com.flab.yousinsa.user.service.exception.SignUpFailException;

@ExtendWith(MockitoExtension.class)
class UserSignUpServiceImplTest {

	@Mock
	UserRepository userRepository;

	@Mock
	SignUpDtoConverter signUpDtoConverter;

	@Mock
	PasswordEncoder passwordEncoder;

	@InjectMocks
	UserSignUpServiceImpl userSignUpServiceImpl;

	UserEntity user;

	final String HashedPassword = "hashedPassword";

	@BeforeEach
	public void setUp() {
		user = new UserEntity("key", "rlfbd5142@gmail.com", "hashedPassword", UserRole.BUYER);
	}

	@UnitTest
	@Test
	@DisplayName("같은 이메일을 가진 유저가 없어 정상적으로 회원가입 되는 경우")
	public void signUpNoUserEmailTest() {
		// given
		SignUpRequestDto signUpRequestDto = new SignUpRequestDto(user.getUserName(), user.getUserEmail(),
			"password", user.getUserRole());
		SignUpResponseDto signUpResponseDto = new SignUpResponseDto(1L, user.getUserName(), user.getUserEmail(),
			user.getUserRole());

		given(userRepository.save(any(UserEntity.class))).willReturn(user);
		given(passwordEncoder.hashPassword(signUpRequestDto.getUserPassword())).willReturn(HashedPassword);
		given(signUpDtoConverter.convertSignUpRequestToUser(any(SignUpRequestDto.class), eq(HashedPassword)))
			.willReturn(user);
		given(signUpDtoConverter.convertUserToSignUpResponse(any(UserEntity.class))).willReturn(signUpResponseDto);

		// when
		SignUpResponseDto resultResponse = userSignUpServiceImpl.trySignUpUser(signUpRequestDto);

		// then
		then(userRepository).should().save(user);
		then(passwordEncoder).should().hashPassword(signUpRequestDto.getUserPassword());
		then(signUpDtoConverter).should().convertSignUpRequestToUser(signUpRequestDto, HashedPassword);
		then(signUpDtoConverter).should().convertUserToSignUpResponse(user);

		assertThat(resultResponse.getUserName()).isEqualTo(signUpResponseDto.getUserName());
		assertThat(resultResponse.getUserEmail()).isEqualTo(signUpResponseDto.getUserEmail());
		assertThat(resultResponse.getUserRole()).isEqualTo(signUpResponseDto.getUserRole());
	}

	@UnitTest
	@Test
	@DisplayName("같은 이메일을 가진 유저가 있어 회원가입이 안되는 경우")
	public void signUpSameEmailUserExistTest() {
		// given
		SignUpRequestDto signUpRequestDto = new SignUpRequestDto(user.getUserName(), user.getUserEmail(),
			"password", user.getUserRole());
		given(userRepository.findByUserEmail(any(String.class))).willReturn(Optional.of(user));

		// when, then
		Assertions.assertThatThrownBy(() -> {
				SignUpResponseDto savedUser = userSignUpServiceImpl.trySignUpUser(signUpRequestDto);
			}).isInstanceOf(SignUpFailException.class)
			.hasMessageContaining("request email is already exists");

		then(userRepository).should().findByUserEmail(signUpRequestDto.getUserEmail());
		then(userRepository).should(never()).save(user);
	}

	@UnitTest
	@Test
	@DisplayName("로그인 하지 않은 상태로 회원 탈퇴 시도")
	public void withDrawUserIfNoLogin() {
		long withDrawUserId = 1L;
		Assertions.assertThatThrownBy(
				() -> userSignUpServiceImpl.tryWithdrawUser(null, withDrawUserId)
			).isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("To withdraw user, user must be logined");
	}

	@UnitTest
	@Test
	@DisplayName("로그인 한 상태로 유효하지 않은 userId 시도")
	public void withDrawUserIfLoginInvalidWithdrawUserId() {
		// given
		AuthUser authUser = new AuthUser(2L, "mockName", "mockEmail@gmail.com", UserRole.BUYER);
		Long withDrawUserId = null;

		Assertions.assertThatThrownBy(
				() -> userSignUpServiceImpl.tryWithdrawUser(authUser, withDrawUserId)
			).isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("To withdraw user, valid withdrawUserId must given");
	}

	@UnitTest
	@Test
	@DisplayName("로그인 한 상태로 해당 유저가 아닌 회원 탈퇴 시도")
	public void withDrawUserIfLoginButDifferentUserId() {
		// given
		AuthUser authUser = new AuthUser(2L, "mockName", "mockEmail@gmail.com", UserRole.BUYER);

		// when
		long withDrawUserId = 1L;
		Assertions.assertThatThrownBy(
				() -> userSignUpServiceImpl.tryWithdrawUser(authUser, withDrawUserId)
			).isInstanceOf(AuthenticationException.class)
			.hasMessageContaining("Withdrawn userId must be equal to logined UserId");
	}

	@UnitTest
	@Test
	@DisplayName("로그인 한 상태로 해당 유저 회원 탈퇴 시도")
	public void withDrawUserIfLoginButSameUserId() {
		// given
		long withDrawUserId = 1L;

		Optional<UserEntity> userEntityOptional = Optional.of(user);
		given(userRepository.findById(anyLong())).willReturn(userEntityOptional);
		willDoNothing().given(userRepository).delete(any(UserEntity.class));
		AuthUser authUser = new AuthUser(withDrawUserId, user.getUserName(), user.getUserEmail(), UserRole.BUYER);

		// when
		userSignUpServiceImpl.tryWithdrawUser(authUser, withDrawUserId);

		then(userRepository).should().findById(withDrawUserId);
		then(userRepository).should().delete(eq(user));
	}

}
