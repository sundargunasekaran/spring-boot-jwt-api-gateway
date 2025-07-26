package com.example.dto;


import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class CustomerModel {

	private int customerId;
	private String customerName;
	private String emailId;
	private String city;
	private String role;
	private String password;
	private int roleId;
	List<PolicyModel> policyModelList = new ArrayList<PolicyModel>();
	
}
