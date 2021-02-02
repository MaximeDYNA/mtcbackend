package com.catis.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.catis.model.CarteGrise;
import com.catis.model.Inspection;
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

    public CarteGrise updateCarteGrise(CarteGrise carteGrise) {
        return cgr.save(carteGrise);
    }

    public List<CarteGrise> findAll() {
        List<CarteGrise> carteGrises = new ArrayList<>();
        cgr.findAll().forEach(carteGrises::add);
        return carteGrises;
    }

    public CarteGrise findCarteGriseById(Long carteGriseId) {
        return cgr.findById(carteGriseId).get();
    }

    public List<CarteGrise> findByImmatriculationOuCarteGrise(String imOrCha) {
        return cgr.findByNumImmatriculationIgnoreCaseOrVehicule_ChassisIgnoreCase(imOrCha, imOrCha);
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
        for (Inspection inspection : inpectionR.findByVisite_StatutAndLigne_IdLigne(3, idLigne)) {
            cgs.add(inspection.getVisite().getCarteGrise());
        }
        return cgs;
    }

    public boolean isCarteGriseExist(String ref) {
        if (findByImmatriculationOuCarteGrise(ref).isEmpty())
            return false;

        return true;
    }


}
