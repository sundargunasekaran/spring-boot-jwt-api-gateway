package com.sd.example.service_discovery_ex;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class ServiceDiscoveryExApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceDiscoveryExApplication.class, args);
	}

}
