package com.flab.yousinsa.interfaces.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import lombok.Data;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class SampleDto {
	private Integer id;
	private String comment;
}
