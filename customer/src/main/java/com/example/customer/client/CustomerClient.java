package com.example.customer.client;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.example.dto.PolicyModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Component
public class CustomerClient {
	
	
	@Autowired
    private RestTemplate template;

    public Map<String, List<PolicyModel>> getAllCustomerPolicyDetails() {
         String json = template.getForObject("http://policy/mcs2/policyapi/customerPolicy",String.class);
         Type type = new TypeToken<Map<String, List<PolicyModel>>>(){}.getType();
         Map<String, List<PolicyModel>> mapOfObjects = new Gson().fromJson(json, type);
        /* Map<String,String> map = new HashMap<String,String>();
         ObjectMapper mapper = new ObjectMapper();

         try {
             //convert JSON string to Map
            map = mapper.readValue(json, new TypeReference< Map<String,String>>(){});
         } catch (Exception e) {
              //logger.info("Exception converting {} to map", json, e);
         }*/
         return mapOfObjects;
    }
    
    public List<PolicyModel> getCustomerPolicyDetails(String customerId) {
        		PolicyModel[] pol = template.getForObject("http://policy/mcs2/policyapi/policyByCustId?customerId=" + customerId,PolicyModel[].class);
        List<PolicyModel> plist = Arrays.asList(pol);
        return plist;
    }

}
