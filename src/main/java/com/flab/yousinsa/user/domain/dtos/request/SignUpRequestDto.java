package com.flab.yousinsa.user.domain.dtos.request;

import javax.validation.constraints.NotEmpty;

import com.flab.yousinsa.user.domain.enums.UserRole;

public class SignUpRequestDto {

	@NotEmpty
	private String userName;

	@NotEmpty
	private String userEmail;

	@NotEmpty
	private String userPassword;

	private UserRole userRole = UserRole.BUYER;

	public SignUpRequestDto() {
	}

	public SignUpRequestDto(String userName, String userEmail, String userPassword, UserRole userRole) {
		this.userName = userName;
		this.userEmail = userEmail;
		this.userPassword = userPassword;
		this.userRole = userRole;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public UserRole getUserRole() {
		return userRole;
	}

	public void setUserRole(UserRole userRole) {
		this.userRole = userRole;
	}
}
