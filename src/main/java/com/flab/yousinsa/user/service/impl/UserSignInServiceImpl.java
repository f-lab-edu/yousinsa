package com.flab.yousinsa.user.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.flab.yousinsa.user.domain.dtos.request.SignInRequestDto;
import com.flab.yousinsa.user.domain.dtos.response.SignInResponseDto;
import com.flab.yousinsa.user.domain.entities.UserEntity;
import com.flab.yousinsa.user.repository.contract.UserRepository;
import com.flab.yousinsa.user.service.PasswordEncoder;
import com.flab.yousinsa.user.service.contract.UserSignInService;
import com.flab.yousinsa.user.service.converter.SignInDtoConverter;
import com.flab.yousinsa.user.service.exception.SignInFailException;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class UserSignInServiceImpl implements UserSignInService {

	private final UserRepository userRepository;
	private final SignInDtoConverter signInDtoConverter;
	private final PasswordEncoder passwordEncoder;

	public UserSignInServiceImpl(UserRepository userRepository, SignInDtoConverter signInDtoConverter,
		PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.signInDtoConverter = signInDtoConverter;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public SignInResponseDto trySignInUser(SignInRequestDto signInRequest) {
		Assert.notNull(signInRequest, "SignInRequestDto must be not null");

		UserEntity findUser = userRepository.findByUserEmail(signInRequest.getUserEmail()).orElseThrow(
			() -> new SignInFailException("request email is not found")
		);

		Boolean isMatched = passwordEncoder.isMatched(signInRequest.getUserPassword(), findUser.getUserPassword());
		if (!isMatched) {
			throw new SignInFailException("password is not valid with email");
		}

		return signInDtoConverter.convertUserToSignInUserResponse(findUser);
	}

}
