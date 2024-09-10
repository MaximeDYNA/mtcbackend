package com.catis.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.catis.model.entity.Vente;
import org.springframework.data.repository.query.Param;

public interface VenteRepository extends CrudRepository<Vente, UUID> {

    Vente findByVisite_IdVisite(UUID idVisite);
    List<Vente> findByActiveStatusTrue(Pageable pageable);
    @Query("select v from Vente v where " +
            ":ref is null or lower(v.numFacture) like lower(concat('%', :ref,'%')) " +
            "or lower(v.visite.carteGrise.proprietaireVehicule.partenaire.nom) like lower(concat('%', :ref,'%')) " +
            "or lower(v.visite.carteGrise.numImmatriculation) like lower(concat('%', :ref,'%'))")
    List<Vente> findByRef(@Param("ref") String name, Pageable pageable);


    // new method to filter tickers just for a certian organisation
    // @Query("select v from Vente v where " +
    //    "(:ref is null or lower(v.numFacture) like lower(concat('%', :ref,'%')) " +
    //    "or lower(v.visite.carteGrise.proprietaireVehicule.partenaire.nom) like lower(concat('%', :ref,'%')) " +
    //    "or lower(v.visite.carteGrise.numImmatriculation) like lower(concat('%', :ref,'%'))) " +
    //    "and v.organisation.organisationId = :organisationId")
    //   Page<Vente> findByRefAndOrganisationId(@Param("ref") String ref, 
    //                                    @Param("organisationId") UUID organisationId, 
    //                                    Pageable pageable);


    @Query("SELECT v FROM Vente v WHERE " +
    "(:search IS NULL OR :search = '' OR " +
    "LOWER(v.numFacture) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
    "LOWER(v.visite.carteGrise.proprietaireVehicule.partenaire.nom) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
    "LOWER(v.visite.carteGrise.numImmatriculation) LIKE LOWER(CONCAT('%', :search, '%'))) " +
    "AND v.organisation.organisationId = :organisationId " +
    "AND v.activeStatus = true")
   Page<Vente> findByRefAndOrganisationId(@Param("search") String search, @Param("organisationId") UUID organisationId, 
                                     Pageable pageable);


    @Query("select v from Vente v where v.createdDate >= CURRENT_DATE ")
    List<Vente> venteOftheDay();

    @Query("select v from Vente v where v.createdDate >= ?1 AND v.createdDate BETWEEN ?1 And ?2")
    List<Vente> ventebyDate(LocalDateTime d, LocalDateTime f);

    List<Vente> findBySessionCaisseCaissierCaissierIdAndCreatedDateGreaterThanOrderByCreatedDateDesc(UUID caissierId, LocalDateTime date);
}
