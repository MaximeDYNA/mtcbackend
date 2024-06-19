package com.catis.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.catis.model.entity.RapportDeVisite;
import com.catis.model.entity.Visite;

public interface RapportDeVisiteRepo extends JpaRepository<RapportDeVisite, UUID> {

    @Query(value = "select r from RapportDeVisite r join fetch r.seuil s "
            + "left join fetch s.lexique l left join fetch l.classification cl "
            + "join fetch s.formule f join fetch f.mesures m where r.visite = ?1")
    List<RapportDeVisite> getRapportDeVisite(Visite visite);

}
