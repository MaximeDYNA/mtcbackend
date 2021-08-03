package com.catis.repository;

import com.catis.model.entity.Intervenant_fraudeType;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface Intervenant_fraudeTypeRepository extends CrudRepository<Intervenant_fraudeType, Long> {

    List<Intervenant_fraudeType> findByActiveStatusTrue();
    List<Intervenant_fraudeType> findByFraudeType_IdAndActiveStatusTrue(Long fraudeId);
}
