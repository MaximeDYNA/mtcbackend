package com.catis.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.catis.model.entity.Inspection;

public interface InspectionRepository extends CrudRepository<Inspection, UUID> {

    Inspection findByVisite_IdVisite(UUID idvisite);

    List<Inspection> findByVisite_StatutAndLigne_IdLigne(int statut, UUID idLigne);

    @Query("select i from Inspection i inner join i.visite v " +
            "where i.ligne.idLigne = ?2 and v.statut = ?1 and i.visibleToTab = true and i.activeStatus = true " +
            "and i.visite.organisation.organisationId = ?3 " +
            "group by i.idInspection " +
            "order by i.createdDate desc")
    List<Inspection> inspectionbyligneAndVisibleToTabTrueAndOrganisation_OrganisationId(int statut, UUID idLigne, UUID orgId);

    List<Inspection> findByVisite_CarteGrise_numImmatriculationOrVisite_CarteGrise_Vehicule_Chassis(String ima, String chassis);

}
