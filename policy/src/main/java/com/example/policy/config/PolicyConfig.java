package com.example.policy.config;

import java.util.Arrays;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@RefreshScope
public class PolicyConfig {
	

	
	
	private String validationQry = "SELECT 1 ";
	
	
	@Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource webDataSource() {
        return (DataSource) DataSourceBuilder.create().build();
    }

    @Bean
    public JdbcTemplate webJdbcTemplate(@Qualifier("webDataSource") DataSource datasource) throws Exception{
    	datasource.setTestWhileIdle(true);
    	datasource.setTimeBetweenEvictionRunsMillis(60000);
    	datasource.setValidationQuery(validationQry);
        return new JdbcTemplate(datasource);
    }


    @Bean
    @LoadBalanced
    public RestTemplate template(){
        return new RestTemplate();
    }

    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:8888"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    
}
