package com.catis.repository;

import org.springframework.data.repository.CrudRepository;

import com.catis.model.entity.Utilisateur;

import java.util.List;
import java.util.UUID;


public interface UtilisateurRepository extends CrudRepository<Utilisateur, UUID> {
    Utilisateur findByKeycloakId(String keycloakId);
    List<Utilisateur> findByActiveStatusTrue();
}
