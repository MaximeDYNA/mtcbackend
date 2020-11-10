package com.catis.service;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.tri.persistence.jpql.QueryBuilder;




@Repository
@Transactional
public class DynamicQueryForViews {
	@PersistenceContext 
	 EntityManager entitymanager;
	 	  
    public DynamicQueryForViews() {
		super();
	}

	public  List<Object[]> getTarget(String tableName, String column){
        String sql="SELECT  "+column+" FROM "+tableName;
        List<Object[]> list = entitymanager.createNativeQuery(sql).getResultList();
        return list;
   
		String searchCriteria = "xyz";
		QueryBuilder builder = new QueryBuilder().select.add(" DISTINCT "+ column).from
		    .add(tableName +" e");
		if (searchCriteria != null) {
		    builder.where.add("e.fieldname2=:fieldName2");
		    builder.setParameter("fieldname2", searchCriteria);
		}
		return builder.createQuery(entityManager, Entity.class).getResultList(); 
	}
}
