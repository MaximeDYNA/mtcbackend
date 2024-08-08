package com.catis.repository;

import com.catis.model.control.Control;
import com.catis.model.entity.Visite;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.catis.model.control.GieglanFile;

public interface GieglanFileRepository extends CrudRepository<GieglanFile, UUID> {

    List<GieglanFile> findByInspection_IdInspection(UUID idInpection);
    List<GieglanFile> findByInspection_IdInspectionAndActiveStatusTrue(UUID idInpection);

    List<GieglanFile> findByCategorieTestLibelleAndInspection_Visite_IdVisite(String libelle, UUID idVisite, Pageable pageable);

    @Query("select g from GieglanFile g inner join fetch g.inspection i "
            + "inner join fetch i.visite v where v = ?1  "
            + " and g.type = 'MEASURE' and g.status <> 'NOT_DEFINED' "
            + "order by v.createdDate desc")
    List<GieglanFile> getMyGieglanFile(Visite visite, Pageable pageable);

    @Query(value = "select g from GieglanFile g inner join fetch g.inspection i "
            + "inner join fetch i.visite v where v.control = ?1  and v <> ?2 "
            + "and g.isAccept = 0 and g.type = 'MEASURE' and g.status = 'VALIDATED' "
            + "order by v.createdDate desc"
    )
    List<GieglanFile> getGieglanFileFailed(Control control, Visite visite);

}
