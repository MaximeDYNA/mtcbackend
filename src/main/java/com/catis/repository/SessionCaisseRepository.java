package com.catis.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.catis.model.SessionCaisse;

public interface SessionCaisseRepository extends CrudRepository<SessionCaisse, String> {

    SessionCaisse findBySessionCaisseId(long id);

    //List<SessionCaisse> findByUser_UtilisateurId(Long userId);
    SessionCaisse findByActiveTrueAndCaissierCaissierId(Long caissierId);
    SessionCaisse findByActiveTrueAndCaissier_User_UtilisateurId(Long userId);
}
