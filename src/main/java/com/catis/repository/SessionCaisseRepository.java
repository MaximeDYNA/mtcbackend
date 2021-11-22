package com.catis.repository;


import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.catis.model.entity.SessionCaisse;

public interface SessionCaisseRepository extends CrudRepository<SessionCaisse, UUID> {

    SessionCaisse findBySessionCaisseId(UUID id);

    List<SessionCaisse> findByActiveStatusTrue();
    List<SessionCaisse> findByActiveStatusTrue(Pageable pageable);
    SessionCaisse findByActiveTrueAndCaissierCaissierId(UUID caissierId);
    SessionCaisse findByActiveTrueAndCaissier_User_UtilisateurId(UUID userId);
    SessionCaisse findByActiveTrueAndCaissier_User_KeycloakId(String keycloakId);


}
