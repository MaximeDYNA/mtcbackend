package com.catis.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;

public class VariableView {
	/* @Autowired 
	 EntityManager entityManager;    

	    public List<String> findSensorDataForDate(String tableName, String column) {
	          // DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd");
	           // String tableName = "data_" + date.format(formatter);

	           Query query = entityManager.createNativeQuery(
	                             "select DISTINCT t."+column+" from "+ tableName + " t");

	           List<String> queryResults = query.getResultList();
	           
	           List<Object> objects = new ArrayList<>();
	           for (String row : queryResults) {
	               System.out.println(row);
	           }

	           return queryResults;
	   }*/
}
