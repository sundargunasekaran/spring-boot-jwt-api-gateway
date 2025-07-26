package com.example.customer.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.customer.client.CustomerClient;
import com.example.customer.service.CustomerService;
import com.example.dto.CustomerModel;
import com.example.dto.PolicyModel;

@RestController
@RequestMapping("/customerapi")
public class CustomerController {
	
	
	@Autowired
	private CustomerService customerServrice;
	
	 @Autowired
	 private CustomerClient customerServiceClient;
	
	//@Autowired
	//private PolicyProxy policyProxy;
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public List<CustomerModel> getAll() {
		List<CustomerModel> customerList =  customerServrice.getAllCustomerDetails();
		Map<String, List<PolicyModel>> customerPolicy =  customerServiceClient.getAllCustomerPolicyDetails();
		List<CustomerModel> cl = customerServrice.getCustomerPolicyDetails(customerList,customerPolicy);
		return cl;
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public CustomerModel getById(@PathVariable String id) {	
		CustomerModel c = new CustomerModel();
		c.setCustomerId(Integer.parseInt(id));
		return customerServrice.getCustomerById(c);
	}
	
	@RequestMapping(value = "/getCustomerPolicy/{id}", method = RequestMethod.GET)
	public CustomerModel getgetCustomerPolicy(@PathVariable String id) {	
		CustomerModel c = new CustomerModel();
		c.setCustomerId(Integer.parseInt(id));
		CustomerModel cm = customerServrice.getCustomerById(c);
		List<PolicyModel> customerPolicyList =  customerServiceClient.getCustomerPolicyDetails(id);
		cm.setPolicyModelList(customerPolicyList);
		return cm;
	}

}
