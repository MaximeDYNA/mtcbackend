package com.catis.repository;

import java.time.LocalDateTime;
import java.util.List;

import com.catis.model.entity.Control;
import com.catis.model.entity.Produit;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.catis.model.entity.Visite;
import org.springframework.stereotype.Repository;

@Repository
public interface VisiteRepository extends CrudRepository<Visite, Long> {


    List<Visite> findByCarteGriseNumImmatriculationIgnoreCaseOrCarteGrise_Vehicule_ChassisIgnoreCaseAndOrganisation_OrganisationId(String imOrCha, String imOrCha2, Long id);

    List<Visite> findByContreVisiteFalse();

    List<Visite> findByOrganisation_OrganisationIdAndActiveStatusTrue(Long id);

    List<Visite> findByActiveStatusTrueAndContreVisiteTrue();

    List<Visite> findByActiveStatusTrue();

    List<Visite> findByActiveStatusTrueAndCarteGriseProduit(Produit produit);

    List<Visite> findByContreVisiteFalseAndCarteGriseNumImmatriculationIgnoreCaseOrCarteGrise_Vehicule_ChassisIgnoreCase(String imOrCha, String imOrCha2);

    List<Visite> findByEncoursTrueOrderByCreatedDateDesc();

    List<Visite> findByEncoursTrueAndStatut(int status, Sort sort);

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
