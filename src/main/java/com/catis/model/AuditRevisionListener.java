package com.catis.model;

import com.catis.objectTemporaire.UserInfoIn;
import org.hibernate.envers.RevisionListener;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public class AuditRevisionListener implements RevisionListener {

    @Autowired
    HttpServletRequest request;
    @Override
    public void newRevision(Object revisionEntity) {
        String currentUser = Optional.ofNullable(UserInfoIn.getUserInfo(request).getLogin())
                .orElse("Unknown-User");
        AuditRevisionEntity audit = (AuditRevisionEntity) revisionEntity;
        audit.setUser(currentUser);
    }
}

