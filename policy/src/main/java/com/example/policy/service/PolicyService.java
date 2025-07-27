package com.example.policy.service;

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

import com.example.dto.PolicyModel;



@Repository
public class PolicyService {
	
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
	
	
	public List<PolicyModel> getAllPoplicyDetails() {
		try{
			List<PolicyModel> modelList = new ArrayList<PolicyModel>();
			 String sql = "SELECT policy_id,policy_name,policy_cc_phone,policy_term_years FROM tst_policy ";


			List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql );
			Map<String, String> objMap = new HashMap<String, String>();
			for (Map row : rows) {
				PolicyModel model = new PolicyModel();
				model.setPolicyId(new BigDecimal(row.get("policy_id").toString()).intValue());
				model.setPolicyContactNo(new BigDecimal(row.get("policy_cc_phone").toString()).intValue());
				model.setPolicyName(row.get("policy_name").toString());
				model.setPolicyYrs(new BigDecimal(row.get("policy_term_years").toString()).intValue());
				modelList.add(model);
			}
			return modelList;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public Map<String, List<PolicyModel>> getCustomerPolicyDetails(PolicyModel policy) {
		try{
			List<PolicyModel> modelList = new ArrayList<PolicyModel>();
			 String sql = "SELECT lup.customer_id,lpp.policy_id,lpp.policy_name,lpp.policy_cc_phone,lpp.policy_term_years " + 
			 		"FROM tst_policy lpp " + 
			 		"LEFT JOIN tst_customer_policy lup on lup.policy_id = lpp.policy_id " ;
			 String orderSql =	"order by lup.customer_id,lpp.policy_id ";
			 if(policy != null && policy.getCustomerId() > 0) {
				String whereSql = "  ";
			    Map<String,Object> qryMap = new LinkedHashMap<String,Object>();
		        ArrayList<Object> paramList = new ArrayList<Object>();
		        if(policy.getPolicyId() > 0 ){
		        	qryMap.put(" lup.customer_id = ? ", policy.getCustomerId());
		        }
		        Map<String,ArrayList<Object>> retmap = this.getWhereClause(qryMap);
		        for (Map.Entry<String, ArrayList<Object>> entry : retmap.entrySet()) {
				    whereSql = entry.getKey();
				    paramList = entry.getValue();
				}
		        sql += whereSql;
			 }

			List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql + orderSql);
			Map<String, List<PolicyModel>> objMap = new HashMap<String, List<PolicyModel>>();
			for (Map row : rows) {
				PolicyModel model = new PolicyModel();
				int customerId = -1;
				if(row.get("customer_id") != null) {
					customerId = new BigDecimal(row.get("customer_id").toString()).intValue();
				}
				
				model.setPolicyId(new BigDecimal(row.get("policy_id").toString()).intValue());
				//model.setPolicyCompany(row.get("company_name").toString());
				model.setPolicyName(row.get("policy_name").toString());
				model.setPolicyContactNo(new BigDecimal(row.get("policy_cc_phone").toString()).intValue());
				model.setPolicyYrs(new BigDecimal(row.get("policy_term_years").toString()).intValue());
				
				if(objMap.containsKey(String.valueOf(customerId))) {
					objMap.get(String.valueOf(customerId)).add(model);
				}else {
					List<PolicyModel> pList = new ArrayList<PolicyModel>();
					pList.add(model);
					objMap.put(String.valueOf(customerId), pList);
				}								
			}
			return objMap;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	
	 public List<PolicyModel> getPolicyByCustomerId(PolicyModel policy){
		 
		 String sql = "SELECT lup.customer_id,lpp.policy_id,lpp.policy_name,lpp.policy_cc_phone,lpp.policy_term_years " + 
			 		"FROM tst_policy lpp " + 
			 		"LEFT JOIN tst_customer_policy lup on lup.policy_id = lpp.policy_id " ;
			 String orderSql =	" order by lup.customer_id,lpp.policy_id ";
		 
			 if(policy.getCustomerId() > 0 ){
				 sql += " where lup.customer_id = "+ policy.getCustomerId();
		        }
			 
		    return jdbcTemplate.query(sql + orderSql,new RowMapper<PolicyModel>(){  
		      @Override  
		      public PolicyModel mapRow(ResultSet rs, int rownumber) throws  SQLException    {  
		    	  PolicyModel bean = new PolicyModel();  
		    	  bean.setPolicyId(rs.getInt("policy_id")); 
                  bean.setPolicyContactNo(rs.getInt("policy_cc_phone"));
                  //bean.setPolicyCompany(rs.getString("company_name"));
                  bean.setPolicyName(rs.getString("policy_name"));
                 // bean.setPolicyEmail(rs.getString("policy_cc_email"));
                  bean.setPolicyYrs(rs.getInt("policy_term_years"));
		            return bean;  
		          }  
		      });  
		      }
	
	
	public PolicyModel getPolicyByCustomer(PolicyModel policy) {
        String sql = "SELECT policy_id,policy_name,policy_cc_phone,policy_term_years FROM tst_policy ";
		try{
			   
			String whereSql = "  ";
			    Map<String,Object> qryMap = new LinkedHashMap<String,Object>();
		        ArrayList<Object> paramList = new ArrayList<Object>();
		        if(policy.getPolicyId() > 0 ){
		        	qryMap.put(" policy_id = ? ", policy.getPolicyId());
		        }
		        Map<String,ArrayList<Object>> retmap = this.getWhereClause(qryMap);
		        for (Map.Entry<String, ArrayList<Object>> entry : retmap.entrySet()) {
				    whereSql = entry.getKey();
				    paramList = entry.getValue();
				}
		        sql += whereSql;
		        //if(paramList.size() > 0){
		        	Object[] param = paramList.toArray(new Object[paramList.size()]);
		        	PolicyModel policyModel = jdbcTemplate.queryForObject(
		                    sql,
		                    param,
		                    new RowMapper<PolicyModel>() {
		                        public PolicyModel mapRow(ResultSet rs, int rowNum) throws SQLException {
		                        	PolicyModel bean = new PolicyModel();
		                            bean.setPolicyId(rs.getInt("policy_id")); 
		                            bean.setPolicyContactNo(rs.getInt("policy_cc_phone"));
		                            //bean.setPolicyCompany(rs.getString("company_name"));
		                            bean.setPolicyName(rs.getString("policy_name"));
		                           // bean.setPolicyEmail(rs.getString("policy_cc_email"));
		                            bean.setPolicyYrs(rs.getInt("policy_term_years"));
		                            /*List<String> list = Arrays.asList(rs.getString("role_name"));
		                            String result = String.join(",", list);*/
		                            return bean;
		                        }
		                    });
		        	return policyModel;
		        /*}else{
		        	 return null;
		        }*/
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
      
        
    }
	
	public PolicyModel getPolicyById(PolicyModel policy) {
        String sql = "SELECT policy_id,policy_name,policy_cc_phone,policy_term_years FROM tst_policy ";
		try{
			    String whereSql = "  ";
			    Map<String,Object> qryMap = new LinkedHashMap<String,Object>();
		        ArrayList<Object> paramList = new ArrayList<Object>();
		        if(policy.getPolicyId() > 0 ){
		        	qryMap.put(" policy_id = ? ", policy.getPolicyId());
		        }
		        Map<String,ArrayList<Object>> retmap = this.getWhereClause(qryMap);
		        for (Map.Entry<String, ArrayList<Object>> entry : retmap.entrySet()) {
				    whereSql = entry.getKey();
				    paramList = entry.getValue();
				}
		        sql += whereSql;
		        //if(paramList.size() > 0){
		        	Object[] param = paramList.toArray(new Object[paramList.size()]);
		        	PolicyModel policyModel = jdbcTemplate.queryForObject(
		                    sql,
		                    param,
		                    new RowMapper<PolicyModel>() {
		                        public PolicyModel mapRow(ResultSet rs, int rowNum) throws SQLException {
		                        	PolicyModel bean = new PolicyModel();
		                            bean.setPolicyId(rs.getInt("policy_id")); 
		                            bean.setPolicyContactNo(rs.getInt("policy_cc_phone"));
		                            //bean.setPolicyCompany(rs.getString("company_name"));
		                            bean.setPolicyName(rs.getString("policy_name"));
		                           // bean.setPolicyEmail(rs.getString("policy_cc_email"));
		                            bean.setPolicyYrs(rs.getInt("policy_term_years"));
		                            /*List<String> list = Arrays.asList(rs.getString("role_name"));
		                            String result = String.join(",", list);*/
		                            return bean;
		                        }
		                    });
		        	return policyModel;
		        /*}else{
		        	 return null;
		        }*/
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
      
        
    }


}
