package com.catis.repository;

import com.catis.model.AuditRevisionEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AuditRepository extends CrudRepository<AuditRevisionEntity, Long> {

   ;

}
