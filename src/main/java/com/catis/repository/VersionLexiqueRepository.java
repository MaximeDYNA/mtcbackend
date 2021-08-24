package com.catis.repository;

import org.springframework.data.repository.CrudRepository;

import com.catis.model.entity.VersionLexique;

public interface VersionLexiqueRepository extends CrudRepository<VersionLexique, Long> {
    VersionLexique findByLibelle(String libelle);
}
