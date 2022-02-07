package com.catis.repository;

import com.catis.model.entity.Ligne;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface LigneRepository extends CrudRepository<Ligne, UUID> {

    List<Ligne> findByActiveStatusTrue();
    List<Ligne> findByActiveStatusTrueAndOrganisation_OrganisationId(Long orgId);
}
