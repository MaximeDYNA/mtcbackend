package com.catis.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.catis.dtoprojections.OrganisationDataDTO;
import com.catis.model.entity.Organisation;

import java.util.List;
import java.util.UUID;

public interface OrganisationRepository extends CrudRepository<Organisation, UUID> {
    @Query("select o from Organisation o inner join o.childOrganisations child inner join child.visites v where o = ?1")
    Organisation findVisitesOfOrganisation(Organisation organisation);
    Page<Organisation> findByActiveStatusTrueAndParentTrue(Pageable pageable);

    List<Organisation> findByActiveStatusTrueAndParentTrue();
    List<Organisation> findByActiveStatusTrueAndParentFalse();
    List<Organisation> findByActiveStatusTrueAndParentOrganisation_ActiveStatusTrueAndParentOrganisation_OrganisationId(UUID id, Pageable pageable);
    List<Organisation> findByActiveStatusTrueAndParentOrganisation_ActiveStatusTrueAndParentOrganisation_OrganisationId(UUID id);
    List<Organisation> findByActiveStatusTrueAndParentOrganisation_ActiveStatusTrueAndParentOrganisation_Nom(String nomOrganisation);
    
    // flemming implimented
    @Query("SELECT o.organisationId AS organisationId, o.nom AS nom " +
    "FROM Organisation o " +
    "WHERE o.activeStatus = true AND o.nom LIKE ?1%")
    List<OrganisationDataDTO> findByActiveStatusTrueAndParentFalse(String nom, Pageable pageable);
    // AND o.parent = false
}
