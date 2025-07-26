package com.authservice.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.authservice.service.UserService;
import com.authservice.utils.JWTUtils;
import com.example.dto.CustomerModel;
import com.example.dto.JwtResponseModel;
import com.example.dto.UserModel;

@Service
public class UserServiceimpl implements UserService{
	
	public static final String JWT_IAT_KEY = "iat";
	public static final String JWT_ID_KEY = "jti";
	
	@Autowired
	private JWTUtils jwtUtils;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public JwtResponseModel getTokenByLogin(String userId) {
		JwtResponseModel jwtModel = new JwtResponseModel();
		jwtModel.setAccessToken(jwtUtils.generateTokenWithClaims(userId,null,null));
		jwtModel.setRefreshToken(jwtUtils.getUserRefreshToken());
		jwtModel.setUserId(userId);
		return jwtModel;
	}
	
	
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
	

	@Override
	public UserModel validateUserLogin(UserModel model) {
        String sql = "SELECT user_id,user_name,password from tst_user  ";
		try{
			    String whereSql = "  ";
			    Map<String,Object> qryMap = new LinkedHashMap<String,Object>();
		        ArrayList<Object> paramList = new ArrayList<Object>();
		        if(model.getUserId() != null && !model.getUserId().trim().equals("")){
		        	qryMap.put(" user_name = ? ", model.getUserId());
		        }
		        if(model.getPassword() != null && !model.getPassword().trim().equals("")){
		        	qryMap.put(" password = ? ", model.getPassword());
		        }
		        
		        Map<String,ArrayList<Object>> retmap = this.getWhereClause(qryMap);
		        for (Map.Entry<String, ArrayList<Object>> entry : retmap.entrySet()) {
				    whereSql = entry.getKey();
				    paramList = entry.getValue();
				}
		        sql += whereSql;
		        String groupBy = " ";
		        //if(paramList.size() > 0){
		        	Object[] param = paramList.toArray(new Object[paramList.size()]);
		        	UserModel usermodel = jdbcTemplate.queryForObject(
		                    sql+groupBy,
		                    param,
		                    new RowMapper<UserModel>() {
		                        public UserModel mapRow(ResultSet rs, int rowNum) throws SQLException {
		                        	UserModel bean = new UserModel();
		                            bean.setUserId(rs.getInt("user_id")+""); 
		                            bean.setUserName(rs.getString("user_name"));
		                            bean.setPassword(rs.getString("password"));
		                           // bean.setEmailId(rs.getString("email_id"));
		                            bean.setRoleName("Superadmin");
		                           // bean.setRoleId(rs.getInt("role_id")+"");
		                            /*List<String> list = Arrays.asList(rs.getString("role_name"));
		                            String result = String.join(",", list);*/
		                            return bean;
		                        }
		                    });
		        	return usermodel;
		        /*}else{
		        	 return null;
		        }*/
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
      
        
    }

}
