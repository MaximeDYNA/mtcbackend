package com.catis.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.catis.model.entity.CarteGrise;
import com.catis.model.entity.Inspection;
import com.catis.repository.CarteGriseRepository;
import com.catis.repository.InspectionRepository;

@Service
public class CarteGriseService {

    @Autowired
    private CarteGriseRepository cgr;

    @Autowired
    private InspectionRepository inpectionR;

    public CarteGrise addCarteGrise(CarteGrise carteGrise) {
        if (cgr.findByNumImmatriculationIgnoreCaseOrVehicule_ChassisIgnoreCase(carteGrise.getNumImmatriculation(),
                carteGrise.getNumImmatriculation()).isEmpty())
            return cgr.save(carteGrise);
        else
            return cgr.findByNumImmatriculationIgnoreCaseOrVehicule_ChassisIgnoreCase(carteGrise.getNumImmatriculation(),

                    carteGrise.getNumImmatriculation()).get(0);
    }

    public CarteGrise save(CarteGrise c){
        c = cgr.save(c);

        return c;
    }

    public void deleteById(Long id){
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

    public CarteGrise findCarteGriseById(Long carteGriseId) {
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

    public List<CarteGrise> findByLigne(Long idLigne) {
        List<CarteGrise> cgs = new ArrayList<>();
        for (Inspection inspection : inpectionR.inspectionbyligneAndVisibleToTabTrue(2, idLigne)) {

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
