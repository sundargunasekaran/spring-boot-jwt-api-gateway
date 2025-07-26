package com.authservice.service;

import com.example.dto.JwtResponseModel;
import com.example.dto.UserModel;

/**
 * @author Sundar G
 * Date: 15/04/2021
 * Time: 02:49 PM
 */

public interface  UserService {
	
	

	public JwtResponseModel getTokenByLogin(String userId);
	
	UserModel validateUserLogin(UserModel model);
}
