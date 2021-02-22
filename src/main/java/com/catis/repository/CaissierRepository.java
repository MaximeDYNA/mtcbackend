package com.catis.repository;

import com.catis.model.Caissier;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CaissierRepository extends CrudRepository<Caissier, Long> {

    List<Caissier> findByUser_KeycloakId(String keycloakId);
}
