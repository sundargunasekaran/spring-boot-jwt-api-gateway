package com.example.customer.config;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import feign.RequestInterceptor;
import feign.RequestTemplate;
public class FeignConfig {


	@Bean
	@LoadBalanced
    public RequestInterceptor customRequestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
            	
            	//template.header("X-Custom-Header", "Value");
                // Example: Propagating Authorization header
                 ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                 if (attributes != null) {
                     HttpServletRequest request = attributes.getRequest();
                     Enumeration<String> headerNames = request.getHeaderNames();
         		    while (headerNames.hasMoreElements()) {
         		        String headerName = headerNames.nextElement();
         		        String headerValue = request.getHeader(headerName);
         		        template.header(headerName,headerValue);
         		    }
                 }
            	
            }
        };
    }
}
