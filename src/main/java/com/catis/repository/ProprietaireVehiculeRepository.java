package com.catis.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.catis.dtoprojections.ProprietaireVehiculeDTO;
import com.catis.model.entity.ProprietaireVehicule;

public interface ProprietaireVehiculeRepository extends CrudRepository<ProprietaireVehicule, UUID> {

    List<ProprietaireVehicule> findByPartenaire_PartenaireId(UUID partenaireId);

    List<ProprietaireVehicule> findByActiveStatusTrue();
    // flemming added
    List<ProprietaireVehicule> findByActiveStatusTrue(Pageable pageable);

    List<ProprietaireVehicule> findByActiveStatusTrueAndPartenaire_NomStartsWithIgnoreCase(String nom);
    

    // flemming added
    @Query("SELECT pv.proprietaireVehiculeId AS proprietaireVehiculeId, p.nom AS nom, p.prenom AS prenom " +
    "FROM ProprietaireVehicule pv " +
    "LEFT JOIN pv.partenaire p " +
    "WHERE pv.activeStatus = true AND p.nom LIKE ?1%")
    List<ProprietaireVehiculeDTO> findByActiveStatusTrueAndPartenaire_NomStartsWithIgnoreCase(String nom, Pageable pageable);
}
