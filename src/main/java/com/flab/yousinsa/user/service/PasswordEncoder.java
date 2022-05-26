package com.flab.yousinsa.user.service;

public interface PasswordEncoder {
	String hashPassword(String rawPassword);
	Boolean isMatched(String rawPassword, String hashedPassword);
}
