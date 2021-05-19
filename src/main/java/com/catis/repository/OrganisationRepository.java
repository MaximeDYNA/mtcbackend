package com.catis.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.catis.model.Organisation;

import java.util.List;

public interface OrganisationRepository extends CrudRepository<Organisation, Long> {
    @Query("select o from Organisation o inner join o.childOrganisations child inner join child.visites v where o = ?1")
    Organisation findVisitesOfOrganisation(Organisation organisation);
    Page<Organisation> findByActiveStatusTrueAndParentTrue(Pageable pageable);

    List<Organisation> findByActiveStatusTrueAndParentTrue();
    List<Organisation> findByActiveStatusTrueAndParentOrganisation_ActiveStatusTrueAndParentOrganisation_OrganisationId(Long id, Pageable pageable);
    List<Organisation> findByActiveStatusTrueAndParentOrganisation_ActiveStatusTrueAndParentOrganisation_OrganisationId(Long id);
    List<Organisation> findByActiveStatusTrueAndParentOrganisation_ActiveStatusTrueAndParentOrganisation_Nom(String nomOrganisation);


}
