package com.catis.repository;

import org.springframework.data.repository.CrudRepository;

import com.catis.model.Controleur;

public interface ControleurRepository extends CrudRepository<Controleur, Long> {

	Controleur findByKeycloakId(String keycloakId);
}
