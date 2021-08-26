package com.catis.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.catis.Controller.VisiteController;
import com.catis.model.entity.Controleur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.catis.model.entity.Inspection;
import com.catis.model.entity.Visite;
import com.catis.repository.InspectionRepository;

@Service
public class InspectionService {

    @Autowired
    private InspectionRepository inspectionR;
    @Autowired
    private VisiteService visiteService;
    @Autowired
    private ProduitService ps;

    @Autowired
    private CategorieTestVehiculeService cat;
    @Autowired
    private GieglanFileService gieglanFileService;

    public Inspection addInspection(Inspection inspection) {
        return inspectionR.save(inspection);
    }

    public List<Inspection> findAllInspection() {
        List<Inspection> inspections = new ArrayList<>();
        inspectionR.findAll().forEach(inspections::add);
        return inspections;
    }

    public Inspection findInspectionById(Long id) {

        return inspectionR.findById(id).get();
    }

    public Inspection findInspectionByVisite(Long idvisite) {
        return inspectionR.findByVisite_IdVisite(idvisite);
    }

    public Inspection findLastByRef(String ref) {
        List<Inspection> inspections = inspectionR.findByVisite_CarteGrise_numImmatriculationOrVisite_CarteGrise_Vehicule_Chassis(ref, ref);
        Inspection inspection = inspections.stream().max(Comparator.comparing(Inspection::getCreatedDate))
                .orElse(null);
        return inspection;
    }

    public Inspection setSignature(Long id, String signature, Controleur controleur) throws IOException {

        System.out.println("id visite " + id + " signature " + signature);
        Inspection inspection = findInspectionByVisite(id);
        inspection.setSignature(signature);
        Visite visite = visiteService.findById(id);
        visite.setStatut(4);
        inspection.setControleur(controleur);
        inspection.setVisite(visiteService.add(visite));
        inspection = inspectionR.save(inspection);

        VisiteController.dispatchEdit(visite, visiteService, gieglanFileService, cat, ps);
        return inspection;

    }

    public void deleteInspection (Long id){
        System.out.println(id +" to delete");
        inspectionR.deleteById(id);
    }
}