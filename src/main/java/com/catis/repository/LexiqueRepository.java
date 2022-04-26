package com.catis.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.catis.model.entity.Lexique;

public interface LexiqueRepository extends CrudRepository<Lexique, String> {
    Lexique findByCode(String code);
    List<Lexique> findByVersionLexique_id(UUID versionLexiqueId);
    List<Lexique> findByVersionLexique_idAndCategorieVehicule_id(UUID versionLexiqueId, UUID categorieVehiculeId);
    List<Lexique> findByActiveStatusTrue();
}
