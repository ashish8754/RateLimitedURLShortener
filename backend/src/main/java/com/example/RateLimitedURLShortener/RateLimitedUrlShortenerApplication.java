package com.example.RateLimitedURLShortener;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootApplication
public class RateLimitedUrlShortenerApplication {

	public static void main(String[] args) {
		SpringApplication.run(RateLimitedUrlShortenerApplication.class, args);
	}

	@Bean
	CommandLineRunner redisTest(StringRedisTemplate redisTemplate) {
		return args ->{
			String pong = redisTemplate.getConnectionFactory().getConnection().ping();
			System.out.println("Redis response" + pong);
		};
	}

}
