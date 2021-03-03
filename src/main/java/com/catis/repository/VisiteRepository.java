package com.catis.repository;

import java.util.List;
import java.util.Optional;

import com.catis.model.Control;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.catis.model.Visite;
import org.springframework.stereotype.Repository;

@Repository
public interface VisiteRepository extends JpaRepository<Visite, Long> {


    List<Visite> findByCarteGriseNumImmatriculationIgnoreCaseOrCarteGrise_Vehicule_ChassisIgnoreCase(String imOrCha, String imOrCha2);

    List<Visite> findByContreVisiteFalse();

    List<Visite> findByContreVisiteFalseAndCarteGriseNumImmatriculationIgnoreCaseOrCarteGrise_Vehicule_ChassisIgnoreCase(String imOrCha, String imOrCha2);

    List<Visite> findByEncoursTrueOrderByCreatedDateDesc();

    List<Visite> findByEncoursTrueAndStatut(int status, Sort sort);

    @Query(value = "select v from Visite v inner join v.rapportDeVisites r "
            + "inner join r.seuil s inner join s.formule f inner join f.mesures m "
            + "inner join r.gieglanFile g where v.control = ?1 and v <> ?2 and g.isAccept = true")
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



}
