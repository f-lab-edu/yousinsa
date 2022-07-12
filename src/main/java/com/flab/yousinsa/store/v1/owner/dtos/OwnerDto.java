package com.flab.yousinsa.store.v1.owner.dtos;

import lombok.Getter;
import lombok.Setter;

public class OwnerDto {

	@Setter
	@Getter
	public static class Post {
		private String storeName;
	}
}
