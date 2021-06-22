package com.catis.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.catis.model.ProprietaireVehicule;

public interface ProprietaireVehiculeRepository extends CrudRepository<ProprietaireVehicule, Long> {

    List<ProprietaireVehicule> findByPartenaire_PartenaireId(Long partenaireId);

    List<ProprietaireVehicule> findByActiveStatusTrue();

}
