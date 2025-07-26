package com.authservice.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.authservice.exception.AuthException;
import com.authservice.service.UserService;
import com.example.dto.JwtResponseModel;
import com.example.dto.UserLoginModel;
import com.example.dto.UserModel;

@RestController

@RequestMapping("/authapi")
public class AuthServiceController {
	
	@Autowired
	private UserService userService;
	
	   @Autowired
	    private AuthenticationManager authenticationManager;
	
	
	   @GetMapping(value = "/greet")
	    public ResponseEntity<?> register(HttpServletRequest request) {
	        return ResponseEntity.ok("hello world");
	    }

	   
	@RequestMapping(method = RequestMethod.POST, value = "/login", headers = "Accept= application/json", produces = "application/json")
	public ResponseEntity<JwtResponseModel> authenticateUser(@RequestBody UserLoginModel model)  {
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(model.getUsername(), model.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		UserModel userModel = (UserModel) authentication.getPrincipal();
		List<String> roles = authentication.getAuthorities().stream().map(item -> item.getAuthority())
		        .collect(Collectors.toList());
		JwtResponseModel jwtModel = userService.getTokenByLogin(userModel.getUserName());
        if (jwtModel == null) {
            //"throw new AuthException("Please logout and make new signin request for user", HttpStatus.SC_UNPROCESSABLE_ENTITY);
            throw new AuthException("Please logout and make new signin request for user", HttpStatus.UNPROCESSABLE_ENTITY);
        }

		return ResponseEntity.ok(new JwtResponseModel(jwtModel.getAccessToken(), jwtModel.getRefreshToken(),userModel.getUserId(),userModel.getUserName(), userModel.getEmailId(), roles));
	}
	

}
