package com.flab.yousinsa.user.controller.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.flab.yousinsa.user.controller.annotation.SessionAuth;
import com.flab.yousinsa.user.domain.dtos.AuthUser;
import com.flab.yousinsa.user.domain.dtos.request.SignInRequestDto;
import com.flab.yousinsa.user.domain.dtos.request.SignUpRequestDto;
import com.flab.yousinsa.user.domain.dtos.response.SignUpResponseDto;
import com.flab.yousinsa.user.service.contract.UserSignUpService;

@RestController
public class UserController {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
	private final UserSignUpService userService;

	public UserController(UserSignUpService userService) {
		this.userService = userService;
	}

	@PostMapping("api/v1/users")
	public ResponseEntity<SignUpResponseDto> signUpUser(@RequestBody SignUpRequestDto signUpRequest) {
		SignUpResponseDto signUpResponse = userService.trySignUpUser(signUpRequest);
		return ResponseEntity.ok(signUpResponse);
	}

	@PostMapping("api/v1/users/sign-in")
	public ResponseEntity<SignInResponseDto> signInUser(@Valid @RequestBody SignInRequestDto signInRequestDto,
		HttpSession httpSession) {
		SignInResponseDto signInResponseDto = userSignInService.trySignInUser(signInRequestDto);
		AuthUser authUser = new AuthUser(
			signInResponseDto.getId(),
			signInResponseDto.getUserName(),
			signInResponseDto.getUserEmail(),
			signInResponseDto.getUserRole()
		);
		httpSession.setAttribute(AUTH_USER, authUser);

		return ResponseEntity.ok(signInResponseDto);
	}

	@DeleteMapping("api/v1/users/sign-out")
	public ResponseEntity<Void> signOutUser(HttpSession httpSession) {
		try {
			httpSession.invalidate();
		} catch (IllegalStateException e) {
			if (log.isErrorEnabled()) {
				log.error("User already signed out", e);
			}

			throw new SignOutFailException("User already signed out", e);
		}

		return ResponseEntity.status(HttpStatus.RESET_CONTENT).build();
	}

	@SessionAuth
	@DeleteMapping("api/v1/users/{userId}")
	public ResponseEntity<Void> withDrawUser(AuthUser user, @PathVariable("userId") Long userId,
		HttpSession httpSession) {
		userSignUpService.tryWithdrawUser(user, userId);

		httpSession.invalidate();

		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
