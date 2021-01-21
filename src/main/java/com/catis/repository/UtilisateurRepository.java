package com.catis.repository;

import org.springframework.data.repository.CrudRepository;

import com.catis.model.Utilisateur;


public interface UtilisateurRepository extends CrudRepository<Utilisateur, Long>{
	Utilisateur findByKeycloakId(String keycloakId);
	
}
