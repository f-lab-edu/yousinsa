package com.flab.yousinsa.user.service.contract;

import com.flab.yousinsa.user.domain.dtos.request.SignUpRequestDto;
import com.flab.yousinsa.user.domain.dtos.response.SignUpResponseDto;

public interface UserSignUpService {
	SignUpResponseDto trySignUpUser(SignUpRequestDto signUpRequest);
}
