package com.flab.yousinsa.store.v1.dtos;

import lombok.Getter;
import lombok.Setter;

public class StoreDto {

	@Setter
	@Getter
	public static class Post {
		private String storeName;
	}
}
