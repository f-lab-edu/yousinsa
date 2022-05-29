package com.flab.yousinsa.global.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "spring.messages")
public class MessageConfig {

	private String basename;
	private String encoding;
	private int cacheDuration;
	private boolean alwaysUseMessageFormat;
	private boolean useCodeAsDefaultMessage;
	private boolean fallbackToSystemLocale;

	@Bean
	public MessageSource messageSource() {

		String prefix = "classpath:";
		String[] suffixes = this.basename.replaceAll("\\s", "").split(",");

		List<String> resources = new ArrayList<>();
		for (String suffix : suffixes) {
			resources.add(prefix + suffix);
		}

		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasenames(resources.toArray(new String[0]));
		messageSource.setDefaultEncoding(this.encoding);
		messageSource.setCacheSeconds(this.cacheDuration);
		messageSource.setAlwaysUseMessageFormat(this.alwaysUseMessageFormat);
		messageSource.setUseCodeAsDefaultMessage(this.useCodeAsDefaultMessage);
		messageSource.setFallbackToSystemLocale(this.fallbackToSystemLocale);
		return messageSource;
	}

	@Bean
	public MessageSourceAccessor messageSourceAccessor() {
		return new MessageSourceAccessor(messageSource());
	}
}
