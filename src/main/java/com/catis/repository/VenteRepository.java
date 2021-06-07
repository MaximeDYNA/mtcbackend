package com.catis.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.catis.model.Vente;

public interface VenteRepository extends CrudRepository<Vente, Long> {

    Vente findByVisite_IdVisite(Long idVisite);
    List<Vente> findByActiveStatusTrue();

    List<Vente> findBySessionCaisseCaissierCaissierIdAndCreatedDateGreaterThanOrderByCreatedDateDesc(Long caissierId, LocalDateTime date);
}
