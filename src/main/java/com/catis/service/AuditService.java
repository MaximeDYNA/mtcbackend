package com.catis.service;

import com.catis.model.AuditRevisionEntity;
import com.catis.model.Visite;

import com.catis.repository.AuditRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.hibernate.envers.query.criteria.MatchMode;
import org.hibernate.metamodel.internal.MetamodelImpl;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AuditService {
    @Autowired
    private AuditRepository at;

    public void getAllRevision(){
        List<AuditRevisionEntity> as = new ArrayList<>();
        at.findAll().forEach(as::add);

        for(AuditRevisionEntity a : as){

            System.out.println("User: " + a.getUser());
            System.out.println("id: " + a.getId());
            System.out.println("date: " + a.getRevisionDate());

        }

    }
    public AuditRevisionEntity findById(Integer id){
        Optional<AuditRevisionEntity> are = at.findById(id);
        if(are.isPresent())
            return are.get();
        return null;
    }

}
