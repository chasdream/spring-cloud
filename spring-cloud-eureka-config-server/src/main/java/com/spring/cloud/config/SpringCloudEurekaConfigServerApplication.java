package com.spring.cloud.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class SpringCloudEurekaConfigServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringCloudEurekaConfigServerApplication.class, args);
	}
}
