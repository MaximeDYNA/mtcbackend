package com.catis.repository;

import com.catis.model.configuration.AuditRevisionEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface AuditRepository extends CrudRepository<AuditRevisionEntity, UUID> {

   Optional<AuditRevisionEntity> findById(UUID i);

}
