package com.catis.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.catis.model.control.Control;
import com.catis.model.entity.Produit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.catis.model.entity.Visite;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VisiteRepository extends CrudRepository<Visite, UUID> {


    List<Visite> findByCarteGriseNumImmatriculationIgnoreCaseOrCarteGrise_Vehicule_ChassisIgnoreCaseAndOrganisation_OrganisationId(String imOrCha, String imOrCha2, UUID id);

    List<Visite> findByActiveStatusTrueAndCarteGriseNumImmatriculationIgnoreCaseOrCarteGrise_Vehicule_ChassisIgnoreCaseAndOrganisation_OrganisationId(String imOrCha, String imOrCha2, UUID id);

    List<Visite> findByContreVisiteFalse();

    List<Visite> findByOrganisation_OrganisationIdAndActiveStatusTrue(UUID id);

    List<Visite> findByActiveStatusTrueAndContreVisiteTrue();

    List<Visite> findByActiveStatusTrue();

    List<Visite> findByActiveStatusTrueAndCarteGriseProduit(Produit produit);

    List<Visite> findByContreVisiteFalseAndCarteGriseNumImmatriculationIgnoreCaseOrCarteGrise_Vehicule_ChassisIgnoreCase(String imOrCha, String imOrCha2);

    @Query("select v from Visite v inner join fetch v.carteGrise c " +
            "inner join fetch c.produit p inner join fetch p.categorieTestProduits cat " +
            "where v.organisation.organisationId = ?1 " +
            "and v.encours = true " +
            "and v.activeStatus = true " +
            "order by v.createdDate desc ")
    List<Visite> getOrganisationVisiteWithTest(UUID orgId, Pageable pageable);

    List<Visite> findByOrganisation_OrganisationIdAndEncoursTrueAndActiveStatusTrue(UUID orgId, Pageable pageable);

    Page<Visite> findByOrganisation_OrganisationIdAndEncoursFalseAndActiveStatusTrueOrderByCreatedDateDesc(UUID orgId, Pageable pageable);

    @Query("select v from Visite v where " +
            "(?1 is null " +
            "or lower(v.carteGrise.proprietaireVehicule.partenaire.nom) like lower(concat('%', ?1,'%')) " +
            "or lower(v.carteGrise.numImmatriculation) like lower(concat('%', ?1,'%'))) " +
            "and v.organisation.organisationId = ?2 " +
            "and v.activeStatus = true " +
            "and v.encours = true")
    List<Visite> findByRef(String name, UUID organisationId, Pageable pageable);

    List<Visite> findByActiveStatusTrueAndCarteGrise_NumImmatriculationContainingIgnoreCaseOrCarteGrise_Vehicule_ChassisContainingIgnoreCaseOrCaissier_Partenaire_NomContainingIgnoreCaseOrCarteGrise_ProprietaireVehicule_Partenaire_NomContainingIgnoreCaseAndOrganisation_NomContainingIgnoreCaseOrderByCreatedDateDesc(String imma, String chassis, String caissier, String proprietaire, String organisation, Pageable pageable);

    List<Visite> findByOrganisation_OrganisationIdAndEncoursFalseAndActiveStatusTrueOrderByCreatedDateDesc(UUID orgId);

    List<Visite> findByOrganisation_OrganisationIdAndEncoursTrueAndActiveStatusTrueOrderByCreatedDateDesc(UUID orgId);

    List<Visite> findByEncoursTrueAndOrganisation_OrganisationIdAndActiveStatusTrueOrderByCreatedDateDesc(UUID orgId);

    List<Visite> findByOrganisation_OrganisationIdAndActiveStatusTrueOrderByCreatedDateDesc(UUID orgId);

    List<Visite> findByActiveStatusTrueAndEncoursTrueAndStatutAndOrganisation_OrganisationId(int status, UUID orgId, Sort sort);

    List<Visite> findByActiveStatusTrueAndContreVisiteFalse();

    @Query(value = "select v from Visite v join fetch v.rapportDeVisites r "
            + "join fetch r.seuil s join fetch s.formule f join fetch f.mesures m "
            + "join fetch r.gieglanFile g where v.control = ?1 and v <> ?2 and g.isAccept = true "
            + "and g.status = 'VALIDATED' order by v.createdDate desc")
    List<Visite> getLastVisiteWithTestIsOk(Control control, Visite visite);

    @Query(value = "select v from Visite v join fetch v.inspection i "
            + "join fetch i.gieglanFiles f where v.control = ?1  and v <> ?2 "
            + "and f.isAccept = 0 and f.type = 'MEASURE' and f.status = 'VALIDATED' "
            + "order by v.createdDate desc"
    )
    List<Visite> getBeforeLastVisite(Control control, Visite visite, Pageable pageable);

    @Query(value = "select v from Visite v inner join v.control c " +
            "inner join v.inspection i inner join i.gieglanFiles g "+
            "inner join g.categorieTest cat inner join v.carteGrise " +
            "ca where ca.numImmatriculation = ?1 ORDER BY v.createdDate desc ")
    List<Visite> getBeforeLastVisiteWithHisControl(String imma, Pageable pageable);

    @Query(value = "select v from Visite v inner join v.inspection i " +
            "inner join i.gieglanFiles g "+
            "inner join g.categorieTest cat where g.type = 'MEASURE' and ( g.status = 'REJECTED' or g.status = 'VALIDATED') ")
    Visite getVisiteWithMesuare(Visite visite);

    @Query(value="select v from Visite v where v.createdDate >= current_date ")
    List<Visite> visitsOfTheDay();

    @Query("select v from Visite v where v.createdDate >= ?1 AND v.createdDate BETWEEN ?1 And ?2")
    List<Visite> visiteByDate(LocalDateTime d, LocalDateTime f);



}
