package com.flab.yousinsa.user.domain.dtos.request;

import javax.validation.constraints.NotEmpty;

public class SignInRequestDto {

	@NotEmpty
	private String userEmail;
	@NotEmpty
	private String userPassword;

	public SignInRequestDto() {
	}

	public SignInRequestDto(String userEmail, String userPassword) {
		this.userEmail = userEmail;
		this.userPassword = userPassword;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
}
