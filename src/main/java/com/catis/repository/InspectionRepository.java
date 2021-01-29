package com.catis.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.catis.model.Inspection;

public interface InspectionRepository extends CrudRepository<Inspection, Long> {

    Inspection findByVisite_IdVisite(Long idvisite);

    List<Inspection> findByVisite_StatutAndLigne_IdLigne(int statut, Long idLigne);

    List<Inspection> findByVisite_CarteGrise_numImmatriculationOrVisite_CarteGrise_Vehicule_Chassis(String ima, String chassis);

}
