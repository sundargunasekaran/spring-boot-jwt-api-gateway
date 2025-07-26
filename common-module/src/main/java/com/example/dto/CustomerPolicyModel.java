package com.example.dto;

import java.util.List;
import java.util.Map;

import javax.websocket.server.ServerEndpoint;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerPolicyModel {
	
	Map<Integer, List<PolicyModel>> customerPolicy;

}
