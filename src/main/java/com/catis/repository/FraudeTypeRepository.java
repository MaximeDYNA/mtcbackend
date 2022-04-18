package com.catis.repository;

import com.catis.model.entity.FraudeType;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FraudeTypeRepository extends CrudRepository<FraudeType, UUID> {

    List<FraudeType> findByActiveStatusTrue();
    Optional<FraudeType> findByCodeAndActiveStatusTrue(String code);
}
