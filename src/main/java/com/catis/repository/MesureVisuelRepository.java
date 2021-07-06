package com.catis.repository;

import com.catis.model.entity.Inspection;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.catis.model.entity.MesureVisuel;

import java.util.List;


public interface MesureVisuelRepository extends CrudRepository<MesureVisuel, Long> {


    MesureVisuel findByGieglanFile_Inspection_VisiteIdVisite(Long id);


    @Query("select m from MesureVisuel m where m.gieglanFile.inspection.idInspection = ?1")
    List<MesureVisuel> byIdInspection(Long idInspection);

    @Query("select m from MesureVisuel m join m.gieglanFile f " +
        "where f.inspection = ?1")
    List<MesureVisuel> getMesureVisuelByInspection(Inspection inspection, Pageable pageable);

}
