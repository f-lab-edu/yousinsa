package com.flab.yousinsa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class YousinsaApplication {

	public static void main(String[] args) {
		SpringApplication.run(YousinsaApplication.class, args);
	}

}
