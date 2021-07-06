package com.catis.service;

import com.catis.model.configuration.AuditRevisionEntity;

import com.catis.objectTemporaire.DaschBoardLogDTO;
import com.catis.objectTemporaire.IdentifierUtil;
import com.catis.repository.AuditRepository;
import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.util.*;

@Service
public class AuditService {
    @Autowired
    private AuditRepository at;
    @PersistenceContext
    private EntityManager em;

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
    public List<DaschBoardLogDTO> getRev(Class<?> clazz) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        AuditReader auditReader = AuditReaderFactory.get(em);
        List<DaschBoardLogDTO> logs = new ArrayList<>();
        DaschBoardLogDTO simpleLog;
        Long d;
        AuditQuery query = auditReader.createQuery().forRevisionsOfEntity(clazz, false, true);
        List a =  query.getResultList();

        List b;
        Object e = clazz.newInstance();
        int j =0;
        DefaultRevisionEntity r1;
        RevisionType r2;
        AuditRevisionEntity audit;

        Class<?> theClass = Class.forName(clazz.getName());


        for(Object i : a){
            Object[] objArray = (Object[]) i;
            System.out.println("forRevisions of Entity "+ToStringBuilder.reflectionToString( i));
            simpleLog = new DaschBoardLogDTO();
            e =  theClass.cast(objArray[0]);
            r1 = (DefaultRevisionEntity)  objArray[1];
            r2 = (RevisionType) objArray[2];
            audit = findById(Integer.valueOf(r1.getId()));
            simpleLog.setAction(r2.name());
            simpleLog.setAuthor(audit.getUser());
            simpleLog.setEntity(clazz.getSimpleName());
            simpleLog.setDate(r1.getRevisionDate());
            d = (Long) em.getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier(e);
            System.out.println("Id présumé a"+em.getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier(e));
            simpleLog.setEntityId(d);
            System.out.println("id's "+d);

            //System.out.println("forEntitiesModifiedAtRevision of Entity "+ToStringBuilder.reflectionToString( b));

            //System.out.println(ToStringBuilder.reflectionToString(simpleLog));
            logs.add(simpleLog);
        }
        return logs;
    }
    public Set<Class<?>> getModelClasses() throws IOException {
        final Set<Class<?>> modelClasses = new HashSet<>();

        final ClassLoader loader = Thread.currentThread()
                .getContextClassLoader();
        final ClassPath classpath = ClassPath.from(loader);
        final String packageName = "com.catis.model.entity";
        final ImmutableSet<ClassPath.ClassInfo> checkstyleClasses = classpath
                .getTopLevelClassesRecursive(packageName);

        for (ClassPath.ClassInfo clazz : checkstyleClasses) {
            final Class<?> loadedClass = clazz.load();
            modelClasses.add(loadedClass);

        }
        return modelClasses;
    }

}
