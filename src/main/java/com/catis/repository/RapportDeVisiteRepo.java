package com.catis.repository;

import java.util.List;

import com.catis.model.Control;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.catis.model.RapportDeVisite;
import com.catis.model.Visite;

public interface RapportDeVisiteRepo extends JpaRepository<RapportDeVisite, Long> {

    @Query(value = "select r from RapportDeVisite r join fetch r.seuil s "
            + "left join fetch s.lexique l left join fetch l.classification cl "
            + "join fetch s.formule f join fetch f.mesures m where r.visite = ?1")
    List<RapportDeVisite> getRapportDeVisite(Visite visite);
}
