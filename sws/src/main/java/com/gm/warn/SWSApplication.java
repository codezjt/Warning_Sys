package com.gm.warn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

@EnableCaching
@SpringBootApplication
@EnableScheduling //定时器注解
@EnableConfigurationProperties
@EnableTransactionManagement
@EnableAsync
public class SWSApplication {

	public static void main(String[] args) {
		SpringApplication.run(SWSApplication.class, args);
	}

	@Primary
	@Bean(name = "restTemplateMsg")
	RestTemplate restTemplate() {
		return new RestTemplate();
	}
}

