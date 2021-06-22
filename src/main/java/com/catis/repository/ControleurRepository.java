package com.catis.repository;

import org.springframework.data.repository.CrudRepository;

import com.catis.model.Controleur;

import java.util.List;

public interface ControleurRepository extends CrudRepository<Controleur, Long> {

    Controleur findByUtilisateur_keycloakId(String keycloakId);

    List<Controleur> findByActiveStatusTrue();
    List<Controleur> findByOrganisation_OrganisationId(Long organisationId);
}
