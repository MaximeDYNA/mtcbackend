package com.catis.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.catis.model.entity.Vente;
import org.springframework.data.repository.query.Param;

public interface VenteRepository extends CrudRepository<Vente, Long> {

    Vente findByVisite_IdVisite(Long idVisite);
    List<Vente> findByActiveStatusTrue(Pageable pageable);
    @Query("select v from Vente v where " +
            ":ref is null or lower(v.numFacture) like lower(concat('%', :ref,'%')) " +
            "or lower(v.visite.cartegrise.proprietaireVehicule.nom) like lower(concat('%', :ref,'%')) " +
            "or lower(v.visite.cartegrise.numImmatriculation) like lower(concat('%', :ref,'%'))")
    List<Vente> findByRef(@Param("ref") String name, Pageable pageable);

    @Query("select v from Vente v where v.createdDate >= CURRENT_DATE ")
    List<Vente> venteOftheDay();

    @Query("select v from Vente v where v.createdDate >= ?1 AND v.createdDate BETWEEN ?1 And ?2")
    List<Vente> ventebyDate(LocalDateTime d, LocalDateTime f);

    List<Vente> findBySessionCaisseCaissierCaissierIdAndCreatedDateGreaterThanOrderByCreatedDateDesc(Long caissierId, LocalDateTime date);
}
