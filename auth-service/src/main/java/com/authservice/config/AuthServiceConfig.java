package com.authservice.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;
@Component
@RefreshScope
@Getter
@Setter
public class AuthServiceConfig {
	
	
	private String validationQry = "SELECT 1 ";
	
	@Value("${jwt.secret}")
    private String secret;


}
