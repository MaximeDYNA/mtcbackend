package com.catis.repository;


import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.catis.model.entity.SessionCaisse;

public interface SessionCaisseRepository extends CrudRepository<SessionCaisse, String> {

    SessionCaisse findBySessionCaisseId(long id);

    List<SessionCaisse> findByActiveStatusTrue();
    List<SessionCaisse> findByActiveStatusTrue(Pageable pageable);
    SessionCaisse findByActiveTrueAndCaissierCaissierId(Long caissierId);
    SessionCaisse findByActiveTrueAndCaissier_User_UtilisateurId(Long userId);
    SessionCaisse findByActiveTrueAndCaissier_User_KeycloakId(String keycloakId);


}
