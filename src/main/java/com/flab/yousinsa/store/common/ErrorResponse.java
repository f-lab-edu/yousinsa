package com.flab.yousinsa.store.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Getter;

@Getter
@JsonPropertyOrder({"isSuccess", "code", "message", "details"})
public class ErrorResponse {

	@JsonProperty("isSuccess")
	private String isSuccess;

	@JsonProperty("code")
	private String code;

	@JsonProperty("message")
	private String message;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonProperty("details")
	private String details;

	public ErrorResponse(String code, String message, String details) {
		this.isSuccess = "false";
		this.code = code;
		this.message = message;
		this.details = details;
	}
}
