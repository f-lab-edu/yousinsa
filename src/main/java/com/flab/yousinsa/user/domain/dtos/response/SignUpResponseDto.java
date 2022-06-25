package com.flab.yousinsa.user.domain.dtos.response;

import com.flab.yousinsa.user.domain.enums.UserRole;

public class SignUpResponseDto {

	private Long id;
	private String userName;
	private String userEmail;
	private UserRole userRole;

	public SignUpResponseDto() {
	}

	public SignUpResponseDto(Long id, String userName, String userEmail, UserRole userRole) {
		this.id = id;
		this.userName = userName;
		this.userEmail = userEmail;
		this.userRole = userRole;
	}

	public Long getId() {
		return id;
	}

	public String getUserName() {
		return userName;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public UserRole getUserRole() {
		return userRole;
	}
}
