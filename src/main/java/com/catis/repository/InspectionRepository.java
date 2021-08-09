package com.catis.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.catis.model.entity.Inspection;

public interface InspectionRepository extends CrudRepository<Inspection, Long> {

    Inspection findByVisite_IdVisite(Long idvisite);

    List<Inspection> findByVisite_StatutAndLigne_IdLigne(int statut, Long idLigne);

    @Query("select i from Inspection i inner join i.visite v " +
            "where i.ligne.idLigne = ?2 and v.statut = ?1 and i.visibleToTab = true and i.activeStatus = true " +
            "group by i.idInspection")
    List<Inspection> inspectionbyligneAndVisibleToTabTrue(int statut, Long idLigne);

    List<Inspection> findByVisite_CarteGrise_numImmatriculationOrVisite_CarteGrise_Vehicule_Chassis(String ima, String chassis);

}
