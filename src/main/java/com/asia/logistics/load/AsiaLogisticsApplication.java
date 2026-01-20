package com.asia.logistics.load;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class AsiaLogisticsApplication {

	public static void main(String[] args) {
		SpringApplication.run(AsiaLogisticsApplication.class, args);
	}

}
