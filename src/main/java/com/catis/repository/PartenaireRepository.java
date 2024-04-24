package com.catis.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.catis.model.entity.Partenaire;



public interface PartenaireRepository extends CrudRepository<Partenaire, UUID> {
    List<Partenaire> findByNomStartsWithIgnoreCase(String nom);

    /*
     * @Query("SELECT p FROM partenaire p WHERE p.telephone = :title% OR p.nom = :title% Or p.prenom = :title% Or p.cni = :title% Or p.passport = :title%"
     * ) List<Partenaire> searchByKeyword(@Param("title") String title);
     */
    List<Partenaire> findByNomStartsWithIgnoreCaseOrPrenomStartsWithIgnoreCaseOrPassportStartsWithIgnoreCaseOrTelephoneStartsWithIgnoreCase(String nom, String prenom, String passport, String telephone);

    List<Partenaire> findByNomIgnoreCase(String nom);

    List<Partenaire> findByCniIgnoreCase(String cni);

    List<Partenaire> findByPassportIgnoreCase(String passport);

    List<Partenaire> findByEmail(String email);

    List<Partenaire> findByNomIgnoreCaseAndPrenomIgnoreCase(String nom, String prenom);
}
