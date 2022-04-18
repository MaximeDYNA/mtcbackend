package com.catis.repository;

import com.catis.model.entity.Intervenant_fraudeType;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface Intervenant_fraudeTypeRepository extends CrudRepository<Intervenant_fraudeType, UUID> {

    List<Intervenant_fraudeType> findByActiveStatusTrue();
    List<Intervenant_fraudeType> findByFraudeType_IdAndActiveStatusTrue(Long UUID);
}
