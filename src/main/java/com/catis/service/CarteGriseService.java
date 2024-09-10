package com.catis.service;

import java.util.*;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;

import com.catis.model.entity.CarteGrise;
import com.catis.model.entity.Inspection;
import com.catis.model.entity.ProprietaireVehicule;
import com.catis.repository.CarteGriseRepository;
import com.catis.repository.ProprietaireVehiculeRepository;
import com.catis.repository.InspectionRepository;

@Service
public class CarteGriseService {

    @Autowired
    private CarteGriseRepository cgr;

    @Autowired
    private InspectionRepository inpectionR;

    @Autowired
    private ProprietaireVehiculeRepository pvr;

    // flemming added
    @Transactional
    public void updateProprietaireVehicule(UUID carteGriseId, UUID newProprietaireVehiculeId) {
        // Fetch the CarteGrise entity
        CarteGrise carteGrise = cgr.findById(carteGriseId)
            .orElseThrow(() -> new EntityNotFoundException("CarteGrise not found"));

        // Fetch the new ProprietaireVehicule entity
        ProprietaireVehicule newProprietaireVehicule = pvr.findById(newProprietaireVehiculeId)
        .orElseThrow(() -> new EntityNotFoundException("ProprietaireVehicule not found"));

        // Update the ProprietaireVehicule of the CarteGrise
        carteGrise.setProprietaireVehicule(newProprietaireVehicule);
        cgr.save(carteGrise);
    }

    // @Transactional(isolation = Isolation.SERIALIZABLE)
    public CarteGrise addCarteGrise(CarteGrise carteGrise) {
        if (cgr.findByNumImmatriculationIgnoreCaseOrVehicule_ChassisIgnoreCase(carteGrise.getNumImmatriculation(),
                carteGrise.getNumImmatriculation()).isEmpty())
            return cgr.save(carteGrise);
        else
            System.out.println("returning existing carteGrise");
            return cgr.findByNumImmatriculationIgnoreCaseOrVehicule_ChassisIgnoreCase(carteGrise.getNumImmatriculation(),

                    carteGrise.getNumImmatriculation()).get(0);
    }

    public CarteGrise save(CarteGrise c){
        System.out.println("saving carteGrise");
        c = cgr.save(c);

        return c;
    }

    public void deleteById(UUID id){
        cgr.deleteById(id);
    }

    public CarteGrise updateCarteGrise(CarteGrise carteGrise) {
        return cgr.save(carteGrise);
    }

    public List<CarteGrise> findAll() {
        List<CarteGrise> carteGrises = new ArrayList<>();
        cgr.findByActiveStatusTrue().forEach(carteGrises::add);
        return carteGrises;
    }
    // flemming implimented
    public Page<CarteGrise> findAllPage(String search, Pageable pageable) {
        return cgr.findByActiveStatusTrueAndSearch(search, pageable);
    }
    // flemming implimented
    // public List<CarteGrise> findAllPage(Pageable pageable) {
    //     List<CarteGrise> carteGrises = new ArrayList<>();
    //     cgr.findByActiveStatusTrue(pageable).forEach(carteGrises::add);
    //     return carteGrises;
    // }

    public CarteGrise findCarteGriseById(UUID carteGriseId) {
        return cgr.findById(carteGriseId).get();
    }


    public List<CarteGrise> findByImmatriculationOuCarteGrise(String imOrCha) {
        return cgr.findByNumImmatriculationIgnoreCaseOrVehicule_ChassisIgnoreCase(imOrCha, imOrCha);
    }
    public CarteGrise findByImmatriculation(String imOrCha) {
        return cgr.findByNumImmatriculationIgnoreCase(imOrCha);
    }

    public CarteGrise findLastByImmatriculationOuCarteGrise(String imOrCha) {

        return cgr.findByNumImmatriculationIgnoreCaseOrVehicule_ChassisIgnoreCase(imOrCha, imOrCha)
                .stream().max(Comparator.comparing(CarteGrise::getCreatedDate)).orElse(null);
    }

    public List<CarteGrise> findBychassis(String chassis) {

        return cgr.findByVehicule_ChassisStartsWithIgnoreCase(chassis);

    }

    public List<CarteGrise> findLastCgBychassis(String chassis) {
        List<CarteGrise> reponse = new ArrayList<>();
        cgr.findByVehicule_ChassisStartsWithIgnoreCaseOrderByCreatedDateDesc(chassis,
                PageRequest.of(0,1))
                .forEach(
                        carteGrise -> reponse.add(carteGrise)
                );
        return reponse;
    }

    public List<CarteGrise> findByLigne(UUID idLigne, UUID orgId) {
        List<CarteGrise> cgs = new ArrayList<>();
        // flemming added page request to this method
        PageRequest pageable = PageRequest.of(0, 20, Sort.by(Sort.Direction.DESC, "createdDate"));
        // for (Inspection inspection : inpectionR.inspectionbyligneAndOrganisation_OrganisationId(2, idLigne, orgId)) {
        for (Inspection inspection : inpectionR.inspectionbyligneAndOrganisation_OrganisationId(2, idLigne, orgId, pageable)) {
            cgs.add(inspection.getVisite().getCarteGrise());
            System.out.println("inspection "+ inspection.getIdInspection()+" visite "+inspection.getVisite().getIdVisite() +" immatriculation "+inspection.getVisite().getCarteGrise().getNumImmatriculation());
        }
        return cgs;
    }

    public List<CarteGrise> findCartegriseForAssurance(String imma){
        Optional<List<CarteGrise>> c = cgr.findCartegriseWithPartOfImma(imma);
        if(c.isPresent())
            return c.get();
        return null;
    }
    public boolean isCarteGriseExist(String ref) {
        if (findByImmatriculationOuCarteGrise(ref).isEmpty())
            return false;

        return true;
    }

    public boolean isCarteGriseHasValidVisite(String immatriculation){
        Optional<CarteGrise> result = cgr.findCGWithOrderedValidControl(immatriculation);
        if(result.isPresent()){
            if(result.get().getControls()
                    .stream().findFirst().isPresent()){
                return true;
            }
        }
        return false;
    }


}
