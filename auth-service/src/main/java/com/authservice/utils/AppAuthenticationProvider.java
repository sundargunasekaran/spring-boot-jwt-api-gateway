package com.authservice.utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.authservice.service.UserService;
import com.example.dto.UserModel;




/**
 * @author Sundar G
 * Date: 16/04/2021
 * Time: 11:48 AM
 */

@Component
public class AppAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    UserService userService;
   
    
    
    public AppAuthenticationProvider() {
       
    }

    @Override
    public Authentication authenticate(Authentication authentication)  throws AuthenticationException {
        String username = authentication.getName();
        Object credentials = authentication.getCredentials();
        if (!(credentials instanceof String)) {
            return null;
        }
        String password = credentials.toString();
        UserModel userModel = new UserModel();
		try {
			userModel.setUserId(username);
			userModel.setPassword(password);
			userModel = userService.validateUserLogin(userModel);
		} catch (Exception e) {
			userModel = null;
			throw new BadCredentialsException("Authentication failed for " + username);
			
		}

        if (userModel == null) {
            throw new BadCredentialsException("Authentication failed for " + username);
        }

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(userModel.getRoleName()));
       // Authentication auth = new UsernamePasswordAuthenticationToken(userModel.getUserId(), userModel.getPassword(), grantedAuthorities);
        Authentication auth = new UsernamePasswordAuthenticationToken(userModel, null, grantedAuthorities);
        return auth;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

	
	
}
