package com.catis.repository;

import org.springframework.data.repository.CrudRepository;

import com.catis.model.entity.VersionLexique;

import java.util.UUID;

public interface VersionLexiqueRepository extends CrudRepository<VersionLexique, UUID> {
    VersionLexique findByLibelle(String libelle);
}
