package com.catis.repository;

import java.time.LocalDateTime;
import java.util.List;

import com.catis.model.control.Control;
import com.catis.model.entity.Produit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.catis.model.entity.Visite;
import org.springframework.stereotype.Repository;

@Repository
public interface VisiteRepository extends CrudRepository<Visite, Long> {


    List<Visite> findByCarteGriseNumImmatriculationIgnoreCaseOrCarteGrise_Vehicule_ChassisIgnoreCaseAndOrganisation_OrganisationId(String imOrCha, String imOrCha2, Long id);

    List<Visite> findByActiveStatusTrueAndCarteGriseNumImmatriculationIgnoreCaseOrCarteGrise_Vehicule_ChassisIgnoreCaseAndOrganisation_OrganisationId(String imOrCha, String imOrCha2, Long id);

    List<Visite> findByContreVisiteFalse();

    List<Visite> findByOrganisation_OrganisationIdAndActiveStatusTrue(Long id);

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
    List<Visite> getOrganisationVisiteWithTest(Long orgId, Pageable pageable);

    List<Visite> findByOrganisation_OrganisationIdAndEncoursTrueAndActiveStatusTrue(Long orgId, Pageable pageable);

    Page<Visite> findByOrganisation_OrganisationIdAndEncoursFalseAndActiveStatusTrueOrderByCreatedDateDesc(Long orgId, Pageable pageable);

    List<Visite> findByEncoursTrueAndActiveStatusTrueAndCarteGrise_NumImmatriculationContainingIgnoreCaseOrCarteGrise_Vehicule_ChassisContainingIgnoreCaseOrCaissier_Partenaire_NomContainingIgnoreCaseOrCarteGrise_ProprietaireVehicule_Partenaire_NomContainingIgnoreCaseAndOrganisation_OrganisationId(String imma, String chassis, String caissier, String proprietaire, Long organisationId, Pageable pageable);

    List<Visite> findByActiveStatusTrueAndCarteGrise_NumImmatriculationContainingIgnoreCaseOrCarteGrise_Vehicule_ChassisContainingIgnoreCaseOrCaissier_Partenaire_NomContainingIgnoreCaseOrCarteGrise_ProprietaireVehicule_Partenaire_NomContainingIgnoreCaseAndOrganisation_NomContainingIgnoreCaseOrderByCreatedDateDesc(String imma, String chassis, String caissier, String proprietaire, String organisation, Pageable pageable);

    List<Visite> findByOrganisation_OrganisationIdAndEncoursFalseAndActiveStatusTrueOrderByCreatedDateDesc(Long orgId);

    List<Visite> findByOrganisation_OrganisationIdAndEncoursTrueAndActiveStatusTrueOrderByCreatedDateDesc(Long orgId);

    List<Visite> findByEncoursTrueAndOrganisation_OrganisationIdAndActiveStatusTrueOrderByCreatedDateDesc(Long orgId);

    List<Visite> findByOrganisation_OrganisationIdAndActiveStatusTrueOrderByCreatedDateDesc(Long orgId);

    List<Visite> findByEncoursTrueAndStatutAndOrganisation_OrganisationId(int status, Long orgId, Sort sort);

    List<Visite> findByActiveStatusTrueAndContreVisiteFalse();

    @Query(value = "select v from Visite v join fetch v.rapportDeVisites r "
            + "join fetch r.seuil s join fetch s.formule f join fetch f.mesures m "
            + "join fetch r.gieglanFile g where v.control = ?1 and v <> ?2 and g.isAccept = true "
            + "and g.status = 'VALIDATED' order by v.idVisite desc")
    List<Visite> getLastVisiteWithTestIsOk(Control control, Visite visite);

    @Query(value = "select v from Visite v join fetch v.inspection i "
            + "join fetch i.gieglanFiles f where v.control = ?1  and v <> ?2 "
            + "and f.isAccept = 0 and f.type = 'MEASURE' and f.status = 'VALIDATED' "
            + "order by v.idVisite desc"
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
