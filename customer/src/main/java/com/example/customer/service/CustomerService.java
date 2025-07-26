package com.example.customer.service;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.example.dto.CustomerModel;
import com.example.dto.PolicyModel;
@Repository
public class CustomerService {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	
	private Map<String,ArrayList<Object>> getWhereClause(Map<String, Object> queryMap) {
		ArrayList<Object> param = new ArrayList<Object>();
		Map<String,ArrayList<Object>> plm = new LinkedHashMap<String,ArrayList<Object>>();
		int i = 1;
		StringBuffer whereClause = new StringBuffer ("");
		for (Map.Entry<String, Object> entry : queryMap.entrySet()) {
		   // System.out.println("--> "+entry.getKey()+"----"+entry.getValue());
		    if (queryMap.size() <= 0)
				return null;
		    if(i == 1 && queryMap.size() >= 1){
		    	whereClause.append (" WHERE ").append (entry.getKey());
				param.add((Object) queryMap.get(entry.getKey()));
		    }else{
		    	whereClause.append (" AND ").append(entry.getKey());
				param.add((Object) queryMap.get(entry.getKey()));
		    }
			
			i++;
		}
		plm.put(whereClause.toString(), param);
		return plm;
	}
	
	
	
	public List<CustomerModel> getAllCustomerDetails() {
		try{
			List<CustomerModel> modelList = new ArrayList<CustomerModel>();
			 String sql = "SELECT lu.customer_id,lu.customer_name, city, email as email_id FROM  tst_customer lu ";
						//"left join loc_user_roles lur on lur.user_id = lu.user_id "+
						//"left join loc_roles lr on lr.role_id = lur.role_id group by lu.user_id ";


			List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql );
			Map<String, String> objMap = new HashMap<String, String>();
			for (Map row : rows) {
				CustomerModel model = new CustomerModel();
				model.setCustomerId(new BigDecimal(row.get("customer_id").toString()).intValue());
				model.setCustomerName(row.get("customer_name").toString());
				model.setCity(row.get("city").toString());
				model.setEmailId(row.get("email_id").toString());
				//model.setRole(row.get("role_name") != null && !row.get("role_name").toString().trim().equals("") ? row.get("role_name").toString() : "");
				modelList.add(model);
			}
			return modelList;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}



	public CustomerModel getCustomerById(CustomerModel customer) {
        String sql = "SELECT lu.customer_id,lu.customer_name,lu.email as email_id,city FROM  tst_customer lu ";
						//"left join loc_user_roles lur on lur.user_id = lu.user_id "+
						//"left join loc_roles lr on lr.role_id = lur.role_id ";
		try{
			    String whereSql = "  ";
			    Map<String,Object> qryMap = new LinkedHashMap<String,Object>();
		        ArrayList<Object> paramList = new ArrayList<Object>();
		        if(customer.getCustomerName() != null && !customer.getCustomerName().trim().equals("")){
		        	qryMap.put(" lu.customer_name = ? ", customer.getCustomerName());
		        }
		        if(customer.getPassword() != null && !customer.getPassword().trim().equals("")){
		        	qryMap.put(" lu.password = ? ", customer.getPassword());
		        }
		        if(customer.getCustomerId() > 0 ){
		        	qryMap.put(" lu.customer_id = ? ", customer.getCustomerId());
		        }
		        Map<String,ArrayList<Object>> retmap = this.getWhereClause(qryMap);
		        for (Map.Entry<String, ArrayList<Object>> entry : retmap.entrySet()) {
				    whereSql = entry.getKey();
				    paramList = entry.getValue();
				}
		        sql += whereSql;
		        String groupBy = " group by lu.customer_id ";
		        //if(paramList.size() > 0){
		        	Object[] param = paramList.toArray(new Object[paramList.size()]);
		        	CustomerModel customerModel = jdbcTemplate.queryForObject(
		                    sql+groupBy,
		                    param,
		                    new RowMapper<CustomerModel>() {
		                        public CustomerModel mapRow(ResultSet rs, int rowNum) throws SQLException {
		                        	CustomerModel bean = new CustomerModel();
		                            bean.setCustomerId(rs.getInt("customer_id")); 
		                            bean.setCustomerName(rs.getString("customer_name"));
		                            bean.setEmailId(rs.getString("email_id"));
		                            bean.setCity(rs.getString("city"));
		                            /*List<String> list = Arrays.asList(rs.getString("role_name"));
		                            String result = String.join(",", list);*/
		                            return bean;
		                        }
		                    });
		        	return customerModel;
		        /*}else{
		        	 return null;
		        }*/
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
      
        
    }



	public List<CustomerModel> getCustomerPolicyDetails(List<CustomerModel> customerList,	Map<String, List<PolicyModel>> customerPolicy) {
		List<CustomerModel> cmList = new ArrayList();
		try {
			
			for(CustomerModel cm : customerList) {
				System.out.println(">>>>>list "+cm.getCustomerId());
				if(customerPolicy != null && customerPolicy.size() > 0 && customerPolicy.containsKey(String.valueOf(cm.getCustomerId()))) {
					List<PolicyModel> pl = customerPolicy.get(String.valueOf(cm.getCustomerId()));
					cm.setPolicyModelList(pl);
					cmList.add(cm);
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
			return cmList;
		}
		// TODO Auto-generated method stub
		return cmList;
	}

}
