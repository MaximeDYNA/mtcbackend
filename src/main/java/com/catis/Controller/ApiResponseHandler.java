package com.catis.Controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ApiResponseHandler {

		    public static ResponseEntity<Object> generateResponse(HttpStatus status, boolean error,String message, Object responseObj) {
		        Map<String, Object> map = new HashMap<String, Object>();
		        
		        try {
		            //map.put("time", new Date());
		            map.put("status", status.value());
		            map.put("success", error);
		            map.put("message", message);;
		            map.put("data", responseObj);

		            return new ResponseEntity<Object>(map,status);
		        } catch (Exception e) {
		            map.clear();
		            //map.put("time", new Date());
		            map.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
		            map.put("success",false);
		            map.put("message", e.getMessage());
		            map.put("data", null);
		            return new ResponseEntity<Object>(map,status);
		        }
		        
		    }
		
}
