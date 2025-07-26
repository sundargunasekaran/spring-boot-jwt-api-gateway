package com.example.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * JwtResponseModel.java
 * @author Sundar G
 * Date : 17-Jun-2021
 * Time : 12:21:57 pm
 */

public class JwtResponseModel {
	private String token;
	private String type = "Bearer";
	private String refreshToken;
	private String userId;
	private String userName;
	private String email;
	private List<String> roles;
	@JsonIgnore
	private Integer tokenId;

	public JwtResponseModel(String accessToken, String refreshToken, String userId, String userName, String email, List<String> roles) {
		this.token = accessToken;
		this.refreshToken = refreshToken;
		this.userId = userId;
		this.userName = userName;
		this.email = email;
		this.roles = roles;
	}

	public JwtResponseModel() {
		// TODO Auto-generated constructor stub
	}

	public String getAccessToken() {
		return token;
	}

	public void setAccessToken(String accessToken) {
		this.token = accessToken;
	}

	public String getTokenType() {
		return type;
	}

	public void setTokenType(String tokenType) {
		this.type = tokenType;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<String> getRoles() {
		return roles;
	}

	public String getRefreshToken() {
	    return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getTokenId() {
		return tokenId;
	}

	public void setTokenId(Integer tokenId) {
		this.tokenId = tokenId;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
	
	
}

