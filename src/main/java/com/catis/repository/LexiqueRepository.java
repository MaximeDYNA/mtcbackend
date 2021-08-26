package com.catis.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.catis.model.entity.Lexique;

public interface LexiqueRepository extends CrudRepository<Lexique, Long> {

    Lexique findByCode(String code);

    List<Lexique> findByVersionLexique_id(Long versionLexiqueId);
    List<Lexique> findByVersionLexique_idAndCategorieVehicule_Id(Long versionLexiqueId, Long categorieVehiculeId);
    List<Lexique> findByActiveStatusTrue();
}
