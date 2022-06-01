package com.flab.yousinsa.user.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class CustomCryptPasswordEncoder implements PasswordEncoder {

	private final String SHA_512 = "SHA-512";

	@Override
	public String hashPassword(String rawPassword) {
		return hashPasswordWithMessageDigest(rawPassword, SHA_512);
	}

	@Override
	public Boolean isMatched(String rawPassword, String hashedPassword) {
		String hashedRawpassword = hashPasswordWithMessageDigest(rawPassword, SHA_512);

		return hashedRawpassword.equals(hashedPassword);
	}

	private String hashPasswordWithMessageDigest(String rawPassword, String algorithm) {
		String hashedPassword;
		try {
			MessageDigest digest = MessageDigest.getInstance(algorithm);
			digest.reset();
			digest.update(rawPassword.getBytes(StandardCharsets.UTF_8));
			hashedPassword = Arrays.toString(digest.digest());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		return hashedPassword;
	}
}
