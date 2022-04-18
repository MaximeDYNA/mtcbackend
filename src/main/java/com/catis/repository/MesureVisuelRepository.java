package com.catis.repository;

import com.catis.model.entity.Inspection;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.catis.model.entity.MesureVisuel;

import java.util.List;
import java.util.UUID;


public interface MesureVisuelRepository extends CrudRepository<MesureVisuel, UUID> {


    MesureVisuel findByGieglanFile_Inspection_VisiteIdVisite(UUID id);


    @Query("select m from MesureVisuel m where m.gieglanFile.inspection.idInspection = ?1")
    List<MesureVisuel> byIdInspection(UUID idInspection);

    @Query("select m from MesureVisuel m join m.gieglanFile f " +
        "where f.inspection = ?1")
    List<MesureVisuel> getMesureVisuelByInspection(Inspection inspection, Pageable pageable);

}
