package com.flab.yousinsa.user.service.impl;

import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.flab.yousinsa.user.domain.dtos.AuthUser;
import com.flab.yousinsa.user.domain.dtos.request.SignUpRequestDto;
import com.flab.yousinsa.user.domain.dtos.response.SignUpResponseDto;
import com.flab.yousinsa.user.domain.entities.UserEntity;
import com.flab.yousinsa.user.repository.contract.UserRepository;
import com.flab.yousinsa.user.service.PasswordEncoder;
import com.flab.yousinsa.user.service.contract.UserSignUpService;
import com.flab.yousinsa.user.service.converter.SignUpDtoConverter;
import com.flab.yousinsa.user.service.exception.AuthenticationException;
import com.flab.yousinsa.user.service.exception.SignUpFailException;
import com.flab.yousinsa.user.service.exception.WithdrawFailException;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class UserSignUpServiceImpl implements UserSignUpService {

	private final UserRepository userRepository;
	private final SignUpDtoConverter signUpDtoConverter;
	private final PasswordEncoder passwordEncoder;

	public UserSignUpServiceImpl(UserRepository userRepository, SignUpDtoConverter signUpDtoConverter,
		PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.signUpDtoConverter = signUpDtoConverter;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public SignUpResponseDto trySignUpUser(SignUpRequestDto signUpRequest) {
		Assert.notNull(signUpRequest, "SignUpRequest must be not null");
		validateSignUpUser(signUpRequest);

		String hashedPassword = passwordEncoder.hashPassword(signUpRequest.getUserPassword());

		UserEntity newUser = signUpDtoConverter.convertSignUpRequestToUser(signUpRequest, hashedPassword);

		UserEntity savedUser = userRepository.save(newUser);

		return signUpDtoConverter.convertUserToSignUpResponse(savedUser);
	}

	@Override
	public void tryWithdrawUser(AuthUser user, Long withdrawUserId) {
		Assert.notNull(user, "To withdraw user, user must be logined");
		Assert.notNull(withdrawUserId, "To withdraw user, valid withdrawUserId must given");

		if (!Objects.equals(user.getId(), withdrawUserId)) {
			throw new AuthenticationException("Withdrawn userId must be equal to logined UserId");
		}

		UserEntity userEntity = userRepository.findById(withdrawUserId).orElseThrow(
			() -> new WithdrawFailException("Withdraw user id does not exist")
		);

		userRepository.delete(userEntity);
	}

	private void validateSignUpUser(SignUpRequestDto signUpRequest) {
		boolean isPresent = userRepository.findByUserEmail(signUpRequest.getUserEmail()).isPresent();
		if (isPresent) {
			log.info("validateSignUser :: " + signUpRequest.getUserEmail());
			throw new SignUpFailException("request email is already exists");
		}
	}

}
