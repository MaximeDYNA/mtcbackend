package com.catis.controller;

import java.util.HashMap;
import java.util.Map;

import com.catis.model.entity.Message;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ApiResponseHandler {

    public static ResponseEntity<Object> generateResponse(HttpStatus status, boolean error, String message, Object responseObj) {
        Map<String, Object> map = new HashMap<String, Object>();

        try {
            //map.put("time", new Date());
            map.put("status", status.value());
            map.put("success", error);
            map.put("message", message);
            map.put("data", responseObj);

            return new ResponseEntity<Object>(map, status);
        } catch (Exception e) {
            map.clear();
            //map.put("time", new Date());
            map.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            map.put("success", false);
            map.put("message", e.getMessage());
            map.put("data", null);
            return new ResponseEntity<Object>(map, status);
        }

    }
    public static ResponseEntity<Object> generateResponseWithAlertLevel(HttpStatus status, boolean error, Message message, Object responseObj) {
        Map<String, Object> map = new HashMap<String, Object>();

        try {
            //map.put("time", new Date());
            map.put("status", status.value());
            map.put("success", error);
            map.put("message", message);
            map.put("data", responseObj);

            return new ResponseEntity<Object>(map, status);
        } catch (Exception e) {
            map.clear();
            //map.put("time", new Date());
            map.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            map.put("success", false);
            map.put("message", e.getMessage());
            map.put("data", null);
            return new ResponseEntity<Object>(map, status);
        }

    }

    public static ResponseEntity<Object> generateResponses(HttpStatus status, boolean error, String message, Object responseObj, Object datas) {
        Map<String, Object> map = new HashMap<String, Object>();

        try {
            //map.put("time", new Date());
            map.put("status", status.value());
            map.put("success", error);
            map.put("message", message);
            ;
            map.put("data", responseObj);
            map.put("datas", datas);
            return new ResponseEntity<Object>(map, status);
        } catch (Exception e) {
            map.clear();
            //map.put("time", new Date());
            map.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            map.put("success", false);
            map.put("message", e.getMessage());
            map.put("data", null);
            map.put("datas", null);
            return new ResponseEntity<Object>(map, status);
        }

    }
    public static ResponseEntity<Object> reponseForGraph(HttpStatus status, boolean error, String message, Object responseObj, Object datas) {
        Map<String, Object> map = new HashMap<String, Object>();

        try {
            //map.put("time", new Date());
            map.put("status", status.value());
            map.put("success", error);
            map.put("message", message);
            ;
            map.put("data", responseObj);
            map.put("histogramme", datas);
            return new ResponseEntity<Object>(map, status);
        } catch (Exception e) {
            map.clear();
            //map.put("time", new Date());
            map.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            map.put("success", false);
            map.put("message", e.getMessage());
            map.put("data", null);
            map.put("datas", null);
            return new ResponseEntity<Object>(map, status);
        }

    }


}
