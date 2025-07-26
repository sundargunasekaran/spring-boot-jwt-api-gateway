package com.authservice.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.authservice.service.UserService;
import com.authservice.utils.JWTUtils;
import com.example.dto.UserModel;

import io.jsonwebtoken.UnsupportedJwtException;

/**
 * @author Sundar G
 * Date: 15/04/2021
 * Time: 02:43 PM
 */
@Component
public class JWTFilter extends OncePerRequestFilter {
	
	//private  HandlerExceptionResolver handlerExceptionResolver;
	
	//@Autowired
   // private HitsLimsBaseService baseService;


    @Autowired
    private JWTUtils jwtUtils;
    
    private String jwt_header = "Authorization";
    


	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		final String authorizationHeader = request.getHeader(jwt_header);		
        String userId = null;
        String jwt = null;
        String application = null;
        String subject = null;
        try {
        	if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                jwt = authorizationHeader.substring(7);                
                
				if (jwt != null  && jwtUtils.validateJwtToken(jwt,request)) {
					userId = jwtUtils.extractKeyClaim(jwt,"userid").toString();
                    application = jwtUtils.extractKeyClaim(jwt,"application").toString();
                    subject = jwtUtils.extractKeyClaim(jwt,"sub").toString();
                    if(subject != null && !subject.equalsIgnoreCase("test")) {
                    	throw new UnsupportedJwtException("Invalid Token "+jwt);
            		}
                    if(userId == null || userId.trim().equals("")) {
                    	throw new UnsupportedJwtException("Invalid Token "+jwt);
                    }

                    if (userId != null && !userId.trim().equals("") && SecurityContextHolder.getContext().getAuthentication() == null) {

                        if (jwtUtils.validateAccessToken(jwt,request)) {
                        	
                        	UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = jwtUtils.getAuthentication(userId,"");   //3
                        	usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                       	 	SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                       	 	request.setAttribute("userId", userId);
                       	 	request.setAttribute("claims", jwtUtils.extractAllClaims(jwt));
							String[] roles = usernamePasswordAuthenticationToken
									.getAuthorities()
									.stream()
									.map(GrantedAuthority::getAuthority)
									.toArray(String[]::new);
							request.setAttribute("roles", roles);
						}else {
                        	throw new UnsupportedJwtException("Invalid Token "+jwt);
                        }
                    }
                }else {
                	throw new UnsupportedJwtException("Invalid Token "+jwt);
                }
            }
        	
        } catch (Exception ex) {
			request.setAttribute("exception", ex);
			System.out.println(ex.getMessage());
		}
        filterChain.doFilter(request, response); 
    }

}
