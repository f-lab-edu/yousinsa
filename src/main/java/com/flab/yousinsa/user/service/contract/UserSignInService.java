package com.flab.yousinsa.user.service.contract;

import com.flab.yousinsa.user.domain.dtos.request.SignInRequestDto;
import com.flab.yousinsa.user.domain.dtos.response.SignInResponseDto;

public interface UserSignInService {
	SignInResponseDto trySignInUser(SignInRequestDto signInRequest);
}
