package com.example.dto;
/**
 * UserLoginModel.java
 * @author Sundar G
 * Date : 24-Jun-2021
 * Time : 3:10:37 pm
 */
public class UserLoginModel {
	
	private String username;
	private String password;
	
	public UserLoginModel(){
		
	}
	
	public UserLoginModel(String username,String password) {
		this.username = username;
		this.password = password;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	

}

