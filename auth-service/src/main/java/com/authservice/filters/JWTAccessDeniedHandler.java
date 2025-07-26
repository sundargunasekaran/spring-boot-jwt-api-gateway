package com.authservice.filters;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.example.dto.ApiResponseModel;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * AccessDeniedHandler.java
 * @author Sundar G
 * Date : 15-Jun-2021
 * Time : 11:01:42 pm
 */
@Component
public class JWTAccessDeniedHandler implements AccessDeniedHandler {
    
	private static final Logger LOGGER = LoggerFactory.getLogger(JWTAccessDeniedHandler.class);
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
	
	@Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException ex) throws IOException, ServletException {

    	LOGGER.error("Access denied error: {}", ex.getMessage());
		
		httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);

       // final Map<String, Object> body = new HashMap<>();
      //  body.put("code", HttpServletResponse.SC_UNAUTHORIZED);
      //  body.put("payload", "You don't have required role to perform this action.");
        
        ApiResponseModel error = new ApiResponseModel();
		StringWriter stackTrace = new StringWriter();
		ex.printStackTrace(new PrintWriter(stackTrace));
		error.setTrace(stackTrace.toString());
		error.setMessage(ex.getMessage());
		error.setPath(httpServletRequest.getRequestURI());
		error.setDate(dateFormat.format(new Date()));
		error.setError(String.valueOf(HttpServletResponse.SC_FORBIDDEN));
		error.setStatus(HttpStatus.FORBIDDEN.value());

        final ObjectMapper mapper = new ObjectMapper();
       // mapper.writeValue(httpServletResponse.getOutputStream(), body);
        mapper.writeValue(httpServletResponse.getOutputStream(),error);
    }

	
}
