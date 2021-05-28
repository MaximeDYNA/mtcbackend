package com.catis.repository;

import org.springframework.data.repository.CrudRepository;

import com.catis.model.Utilisateur;

import java.util.List;


public interface UtilisateurRepository extends CrudRepository<Utilisateur, Long> {
    Utilisateur findByKeycloakId(String keycloakId);
    List<Utilisateur> findByActiveStatusTrue();
}
