package com.catis.repository;

import com.catis.model.Inspection;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.catis.model.MesureVisuel;

import java.util.List;


public interface MesureVisuelRepository extends CrudRepository<MesureVisuel, Long> {

    /*
    MesureVisuel findByInspection_VisiteIdVisite(Long id);


    @Query("select m from MesureVisuel m where m.inspection.idInspection = ?1")
    List<MesureVisuel> byIdInspection(Long idInspection);*/

    @Query("select m from MesureVisuel m join m.gieglanFile f " +
        "where f.inspection = ?1")
    List<MesureVisuel> getMesureVisuelByInspection(Inspection inspection, Pageable pageable);

}
