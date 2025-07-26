package com.example.policy.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.dto.PolicyModel;
import com.example.policy.service.PolicyService;



@RestController
@RequestMapping("/policyapi")
public class PolicyController {
	
	@Autowired
	PolicyService policyServrice;
	
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public List<PolicyModel> getAll() {
		return policyServrice.getAllPoplicyDetails();
	}
	
	@RequestMapping(value = "/customerPolicy", method = RequestMethod.GET)
	public Map<Integer, List<PolicyModel>> getCustomerPolicyDetails(PolicyModel policy) {
		return policyServrice.getCustomerPolicyDetails(policy);
	}
	
	@RequestMapping(value = "/policyByCustId", method = RequestMethod.GET)
	public List<PolicyModel> getpolicyByCustId(@ModelAttribute("policy")  PolicyModel policy) {
		return policyServrice.getPolicyByCustomerId(policy);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public PolicyModel getById(@PathVariable String id) {	
		PolicyModel p = new PolicyModel();
		p.setPolicyId(Integer.parseInt(id));
		return policyServrice.getPolicyById(p);
	}

}
