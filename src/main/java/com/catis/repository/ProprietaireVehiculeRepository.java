package com.catis.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.catis.model.entity.ProprietaireVehicule;

public interface ProprietaireVehiculeRepository extends CrudRepository<ProprietaireVehicule, UUID> {

    List<ProprietaireVehicule> findByPartenaire_PartenaireId(UUID partenaireId);

    List<ProprietaireVehicule> findByActiveStatusTrue();

    List<ProprietaireVehicule> findByActiveStatusTrueAndPartenaire_NomStartsWithIgnoreCase(String nom);

}
