package com.example.dto;

import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
@AllArgsConstructor
@RequiredArgsConstructor
@Setter
@Getter
public class ApiResponseModel {

	private Integer status;
	private String message;
	private String error;
	private String date;
	private String path;
	private String trace;
	@JsonIgnore
	private String method;
	

}