package com.catis.repository;

import org.springframework.data.repository.CrudRepository;

import com.catis.model.entity.Controleur;

import java.util.List;
import java.util.UUID;

public interface ControleurRepository extends CrudRepository<Controleur, UUID> {

    Controleur findByUtilisateur_keycloakId(String keycloakId);

    List<Controleur> findByActiveStatusTrue();
    List<Controleur> findByOrganisation_OrganisationId(UUID organisationId);
}
