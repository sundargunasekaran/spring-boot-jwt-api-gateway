package com.example.dto;





import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * @author Sundar G
 * Date: 15/04/2021
 * Time: 02:56 PM
 */
@AllArgsConstructor
@RequiredArgsConstructor
@Setter
@Getter
public class UserModel {
	private static final long serialVersionUID = 1L;
	
	private String userId;
	private String userName;
	private String emailId;
	private String password;
	private String roleId;
	private String roleName;
	private String department;	
	
	public UserModel(String userId, String userName, String emailId) {
		this.userId = userId;
		this.userName = userName;
		this.emailId = emailId;
	}
}

