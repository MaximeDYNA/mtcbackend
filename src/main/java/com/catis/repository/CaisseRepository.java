package com.catis.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.catis.model.entity.Caisse;


import java.util.List;
import java.util.UUID;

public interface CaisseRepository extends CrudRepository<Caisse, UUID> {
    List<Caisse> findByActiveStatusTrue();
    // List<Caisse> findByActiveStatusTrue(Pageable pageable);
    Page<Caisse> findByActiveStatusTrue(Pageable pageable);

      // Search by libelle or organisation name with pagination support
      Page<Caisse> findByActiveStatusTrueAndLibelleContainingIgnoreCaseOrOrganisationNomContainingIgnoreCase(String libelle,String organisationNom, Pageable pageable);
     
      // Search by libelle or organisation name with pagination support
      Page<Caisse> findByActiveStatusTrueAndLibelleContainingIgnoreCase(String libelle, Pageable pageable);

}
