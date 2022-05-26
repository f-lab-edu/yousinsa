package com.flab.yousinsa.user.service.impl;

import com.flab.yousinsa.user.domain.dtos.request.SignUpRequestDto;
import com.flab.yousinsa.user.domain.dtos.response.SignUpResponseDto;
import com.flab.yousinsa.user.domain.entities.UserEntity;
import com.flab.yousinsa.user.repository.contract.UserRepository;
import com.flab.yousinsa.user.service.PasswordEncoder;
import com.flab.yousinsa.user.service.contract.UserSignUpService;

import com.flab.yousinsa.user.service.converter.SignUpDtoConverter;

import com.flab.yousinsa.user.service.exception.SignUpFailException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Service
@Transactional
public class UserService implements UserSignUpService {

	private final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

	private final UserRepository userRepository;
	private final SignUpDtoConverter signUpDtoConverter;
	private final PasswordEncoder passwordEncoder;

	public UserService(UserRepository userRepository, SignUpDtoConverter signUpDtoConverter,
		PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.signUpDtoConverter = signUpDtoConverter;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public SignUpResponseDto trySignUpUser(SignUpRequestDto signUpRequest) {
		Assert.notNull(signUpRequest, "SignUpRequest must be not null");
		validateSignUser(signUpRequest);

		String hashedPassword = passwordEncoder.hashPassword(signUpRequest.getUserPassword());
		signUpRequest.setUserPassword(hashedPassword);

		UserEntity newUser = signUpDtoConverter.convertSignUpRequestToUser(signUpRequest);

		UserEntity savedUser = userRepository.save(newUser);

		return signUpDtoConverter.convertUserToSignUpResponse(savedUser);
	}

	private void validateSignUser(SignUpRequestDto signUpRequest) {
		boolean isPresent = userRepository.findByUserEmail(signUpRequest.getUserEmail()).isPresent();
		if (isPresent) {
			LOGGER.info("validateSignUser :: " + signUpRequest.getUserEmail());
			throw new SignUpFailException("request email is already exists");
		}
	}
}
