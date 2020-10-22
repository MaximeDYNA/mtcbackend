package com.catis.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;

import com.catis.model.Partenaire;

public interface PartenaireRepository extends CrudRepository<Partenaire, String>{
	List<Partenaire> findByNomStartsWithIgnoreCase(String nom);
	/*
	 * @Query("SELECT p FROM partenaire p WHERE p.telephone = :title% OR p.nom = :title% Or p.prenom = :title% Or p.cni = :title% Or p.passport = :title%"
	 * ) List<Partenaire> searchByKeyword(@Param("title") String title);
	 */
	List<Partenaire> findByNomStartsWithIgnoreCaseOrPrenomStartsWithIgnoreCaseOrPassportStartsWithIgnoreCaseOrTelephoneStartsWithIgnoreCase(String nom, String prenom, String passport, String telephone);
}
