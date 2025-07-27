package com.example.policy.filter;

import java.io.IOException;
import java.security.SignatureException;
import java.util.Enumeration;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Sundar G
 * Date: 15/04/2021
 * Time: 02:43 PM
 */
@Component
public class JWTFilter extends GenericFilterBean  {
	
	
	@Value("${gateway.url}")
	private String gatewayUrl;
	
	//private  HandlerExceptionResolver handlerExceptionResolver;
	
	//@Autowired
   // private HitsLimsBaseService baseService;


   // @Autowired
   // private JWTUtils jwtUtils;
    
    private String jwt_header = "Authorization";
   // private String gatewayHost = "localhost:8888";

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		 HttpServletRequest request = (HttpServletRequest) req;
		    HttpServletResponse response = (HttpServletResponse) resp;

		    String proxyForwardedHostHeader = request.getHeader("X-Forwarded-Host");
		    Enumeration<String> headerNames = request.getHeaderNames();
		    while (headerNames.hasMoreElements()) {
		        String headerName = headerNames.nextElement();
		        String headerValue = request.getHeader(headerName);
		       System.out.println("Policy service ---------"+headerName +"<><>"+headerValue);
		    }

		    if (proxyForwardedHostHeader == null || !proxyForwardedHostHeader.equals(gatewayUrl)) {
		       
		        byte[] responseToSend =  new ObjectMapper().writeValueAsString("Unauthorized Access, you should pass through the API gateway").getBytes();
		        ((HttpServletResponse) response).setHeader("Content-Type", "application/json");
		        ((HttpServletResponse) response).setStatus(401);
		        response.getOutputStream().write(responseToSend);
		        return;
		    }
		    chain.doFilter(request, response);
		
	}
	
}
