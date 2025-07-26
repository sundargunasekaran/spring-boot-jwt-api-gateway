package com.test.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.test.example.filter.AuthenticationFilter;

@Configuration
public class ServiceGatewayConfig {

	
	 @Bean
	    public RestTemplate template(){
	       return new RestTemplate();
	    }
	 
	 
	 /*

	    @Autowired
	    AuthenticationFilter filter;

	    @Bean
	    public RouteLocator routes(RouteLocatorBuilder builder) {
	        return builder.routes()
	                .route("user-service", r -> r.path("/users/**")
	                        .filters(f -> f.filter(filter))
	                        .uri("lb://user-service"))

	                .route("auth-service", r -> r.path("/auth/**")
	                        .filters(f -> f.filter(filter))
	                        .uri("lb://auth-service"))
	                .build();
	    }
	    
	    */
	 
	 
	 @Autowired
		private AuthenticationFilter filter;

		@Bean
		public RouteLocator routes(RouteLocatorBuilder builder) {
			return builder.routes().route("auth-service", r -> r.path("/mcs3/**").filters(f -> f.filter(filter)).uri("lb://auth-service"))
					//.route("alert", r -> r.path("/alert/**").filters(f -> f.filter(filter)).uri("lb://alert"))
					.route("customer", r -> r.path("/mcs1/**").filters(f -> f.filter(filter)).uri("lb://customer"))
					.route("policy", r -> r.path("/mcs2/**").filters(f -> f.filter(filter)).uri("lb://policy")).build();
		}
	
}
