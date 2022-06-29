package com.flab.yousinsa.user.domain.dtos;

import com.flab.yousinsa.user.domain.enums.UserRole;

public class AuthUser {
	private Long id;
	private String userName;
	private String userEmail;
	private UserRole userRole;

	public AuthUser(Long id, String userName, String userEmail, UserRole userRole) {
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
