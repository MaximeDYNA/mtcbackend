package com.catis.repository;

import com.catis.model.entity.Ligne;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LigneRepository extends CrudRepository<Ligne, Long> {

    List<Ligne> findByActiveStatusTrue();
    List<Ligne> findByActiveStatusTrueAndOrganisation_OrganisationId(Long orgId);
}
