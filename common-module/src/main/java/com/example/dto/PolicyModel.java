package com.example.dto;

import java.util.ArrayList;
import java.util.*;

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
public class PolicyModel {

	private int policyId;
	private String policyName;
	private String policyCompany;
	private String policyEmail;
	private int policyYrs;
	private int policyContactNo;
	private CustomerModel customerModel;
	private int customerId;
	private List<CustomerModel> customerList = new ArrayList<CustomerModel>();
}
