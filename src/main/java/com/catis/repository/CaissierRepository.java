package com.catis.repository;

import com.catis.model.entity.Caissier;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CaissierRepository extends CrudRepository<Caissier, UUID> {

    List<Caissier> findByUser_KeycloakId(String keycloakId);
    Optional<Caissier> findByUser_Login(String login);
    List<Caissier> findByActiveStatusTrue();
    List<Caissier> findByActiveStatusTrue(Pageable pageable);
    List<Caissier> findByCaisse_caisseId(UUID id);
}
