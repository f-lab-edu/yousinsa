package com.flab.yousinsa.store.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Getter;

@JsonPropertyOrder({"isSuccess", "code", "message", "result"})
@Getter
public class BaseResponse<T> {

	@JsonProperty("isSuccess")
	private String isSuccess;

	@JsonProperty("code")
	private String code;

	@JsonProperty("message")
	private String message;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonProperty("result")
	private T result;

	public BaseResponse(String code, String message) {
		this.isSuccess = "true";
		this.code = code;
		this.message = message;
	}

	public BaseResponse(String code, String message, T result) {
		this.isSuccess = "true";
		this.code = code;
		this.message = message;
		this.result = result;
	}
}
