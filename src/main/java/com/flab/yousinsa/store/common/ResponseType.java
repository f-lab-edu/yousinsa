package com.flab.yousinsa.store.common;

public enum ResponseType {

	SUCCESS("success"),

	MESSAGE_TEST("message.test"),
	EXCEPTION_TEST("exception.test");

	private final String prefix;

	ResponseType(String prefix) {
		this.prefix = prefix;
	}

	public String getCode() {
		return prefix + ".code";
	}

	public String getMessage() {
		return prefix + ".message";
	}
}
