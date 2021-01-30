package com.catis.repository;

import java.util.List;
import java.util.Optional;

import com.catis.model.Control;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.catis.model.Visite;

public interface VisiteRepository extends CrudRepository<Visite, Long> {


    List<Visite> findByCarteGriseNumImmatriculationIgnoreCaseOrCarteGrise_Vehicule_ChassisIgnoreCase(String imOrCha, String imOrCha2);

    List<Visite> findByContreVisiteFalse();

    List<Visite> findByContreVisiteFalseAndCarteGriseNumImmatriculationIgnoreCaseOrCarteGrise_Vehicule_ChassisIgnoreCase(String imOrCha, String imOrCha2);

    List<Visite> findByEncoursTrueOrderByCreatedDateDesc();

    List<Visite> findByEncoursTrueAndStatut(int status, Sort sort);

    @Query(value = "select v from Visite v inner join v.rapportDeVisites r "
            + "inner join r.seuil s inner join s.formule f inner join f.mesures m "
            + "inner join r.gieglanFile g where v.control = ?1 and v <> ?2 and g.isAccept = true")
    List<Visite> getLastVisiteWithTestIsOk(Control control, Visite visite);

    @Query(value = "select v from Visite v inner join v.control c " +
        "inner join v.inspection i inner join i.gieglanFile g "+
        "inner join g.categorieTest cat where c = ?1 and v <> ?2 and " +
        "g.isAccept = false ORDER BY v.createdDate desc ")
    List<Visite> getBeforeLastVisite(Control control, Visite visite, Pageable pageable);


}
