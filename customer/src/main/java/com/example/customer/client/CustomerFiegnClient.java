package com.example.customer.client;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.customer.config.FeignConfig;
import com.example.dto.PolicyModel;

@FeignClient(name = "policy", path = "/mcs2/policyapi",configuration = FeignConfig.class)
public interface CustomerFiegnClient {
	
	
	@GetMapping(value = "/customerPolicy")
	public Map<String, List<PolicyModel>> getCustomerPolicyDetails();
	

	@GetMapping(value = "/policyByCustId")
    public List<PolicyModel> getCustomerPolicyDetails(@RequestParam(value="customerId") String customerId) ;

 
}
