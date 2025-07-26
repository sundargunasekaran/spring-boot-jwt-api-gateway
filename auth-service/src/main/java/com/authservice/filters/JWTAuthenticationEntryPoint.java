package com.authservice.filters;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.example.dto.ApiResponseModel;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * JWTAuthenticationEntryPoint.java
 * @author Sundar G
 * Date : 15-Jun-2021
 * Time : 10:25:10 pm
 */


@Component
public class JWTAuthenticationEntryPoint implements AuthenticationEntryPoint {

	
	private static final Logger LOGGER = LoggerFactory.getLogger(JWTAuthenticationEntryPoint.class);
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse httpServletResponse,
			AuthenticationException authException) throws IOException, ServletException {
		String message;
		httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        final Exception exception = (Exception) request.getAttribute("exception");
        if (exception != null) {
        	message = exception.getMessage();
        	if(request.getAttribute("message") != null && !request.getAttribute("message").toString().trim().equals("")) {
        		message = request.getAttribute("message").toString().trim();
        	}
		}else {
			message = authException.getMessage();
		}
        
        ApiResponseModel error = new ApiResponseModel();
		StringWriter stackTrace = new StringWriter();
		authException.printStackTrace(new PrintWriter(stackTrace));
		error.setTrace(stackTrace.toString());
		error.setMessage(message);
		error.setPath(request.getRequestURI());
		error.setDate(dateFormat.format(new Date()));
		error.setError(String.valueOf(HttpServletResponse.SC_UNAUTHORIZED));
		error.setStatus(HttpStatus.UNAUTHORIZED.value());

        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(httpServletResponse.getOutputStream(),error);
		
	}
	
}

