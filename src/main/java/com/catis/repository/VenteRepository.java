package com.catis.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.catis.model.entity.Vente;

public interface VenteRepository extends CrudRepository<Vente, Long> {

    Vente findByVisite_IdVisite(Long idVisite);
    List<Vente> findByActiveStatusTrue();
    List<Vente> findByActiveStatusTrueAndNumFactureStartingWith(String ref);

    @Query("select v from Vente v where v.createdDate >= CURRENT_DATE ")
    List<Vente> venteOftheDay();

    @Query("select v from Vente v where v.createdDate >= ?1 AND v.createdDate BETWEEN ?1 And ?2")
    List<Vente> ventebyDate(LocalDateTime d, LocalDateTime f);

    List<Vente> findBySessionCaisseCaissierCaissierIdAndCreatedDateGreaterThanOrderByCreatedDateDesc(Long caissierId, LocalDateTime date);
}
