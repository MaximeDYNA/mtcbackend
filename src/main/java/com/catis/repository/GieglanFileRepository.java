package com.catis.repository;

import org.springframework.data.domain.Pageable;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.catis.model.GieglanFile;

public interface GieglanFileRepository extends CrudRepository<GieglanFile, Long> {

    List<GieglanFile> findByInspection_IdInspection(Long idInpection);
    List<GieglanFile> findByInspection_IdInspectionAndActiveStatusTrue(Long idInpection);

    List<GieglanFile> findByCategorieTestLibelleAndInspection_Visite_IdVisite(String libelle, Long idVisite, Pageable pageable);
}
