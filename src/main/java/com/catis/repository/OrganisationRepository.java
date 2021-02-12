package com.catis.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.catis.model.Organisation;

public interface OrganisationRepository extends CrudRepository<Organisation, Long> {
    @Query("select o from Organisation o inner join o.childOrganisations child inner join child.visites v where o = ?1")
    Organisation findVisitesOfOrganisation(Organisation organisation);

}
