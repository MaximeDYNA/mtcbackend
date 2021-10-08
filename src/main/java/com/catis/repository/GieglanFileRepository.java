package com.catis.repository;

import com.catis.model.control.Control;
import com.catis.model.entity.Visite;
import org.springframework.data.domain.Pageable;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.catis.model.control.GieglanFile;

public interface GieglanFileRepository extends CrudRepository<GieglanFile, Long> {

    List<GieglanFile> findByInspection_IdInspection(Long idInpection);
    List<GieglanFile> findByInspection_IdInspectionAndActiveStatusTrue(Long idInpection);

    List<GieglanFile> findByCategorieTestLibelleAndInspection_Visite_IdVisite(String libelle, Long idVisite, Pageable pageable);

    @Query("select g from GieglanFile g inner join fetch g.inspection i "
            + "inner join fetch i.visite v where v = ?1  "
            + " and g.type = 'MEASURE' and g.status <> 'NOT_DEFINED' "
            + "order by v.idVisite desc")
    List<GieglanFile> getMyGieglanFile(Visite visite, Pageable pageable);

    @Query(value = "select g from GieglanFile g inner join fetch g.inspection i "
            + "inner join fetch i.visite v where v.control = ?1  and v <> ?2 "
            + "and g.isAccept = 0 and g.type = 'MEASURE' and g.status = 'VALIDATED' "
            + "order by v.idVisite desc"
    )
    List<GieglanFile> getGieglanFileFailed(Control control, Visite visite);
}
