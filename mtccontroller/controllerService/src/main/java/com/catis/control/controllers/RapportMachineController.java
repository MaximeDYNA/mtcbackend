package com.catis.control.controllers;

import com.catis.control.dao.ConstructorModelDao;
import com.catis.control.dao.RapportDeVisiteDao;
import com.catis.control.dao.VisiteDao;
import com.catis.control.dto.DataRapportDto;
import com.catis.control.dto.ExceptionResponse;
import com.catis.control.dto.conformityResponse;
import com.catis.control.entities.ConstructorModel;
import com.catis.control.entities.Ligne;
import com.catis.control.entities.Visite;
import com.catis.control.services.ScheduledService;
import com.catis.control.services.autovisionRapportImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@CrossOrigin("*")
public class RapportMachineController {

    @Autowired
    private autovisionRapportImpl autovisionRapport;

    @Autowired
    private RapportDeVisiteDao rapportDeVisiteDao;

    @Autowired
    private VisiteDao visiteDao;

    @Autowired
    private ConstructorModelDao modelDao;

    @Autowired
    private ScheduledService scheduledService;

    private static Logger log = LoggerFactory.getLogger(RapportMachineController.class);

    @PostMapping(path = "/check/conformity/{id}")
    public ResponseEntity<?> conformity(@PathVariable("id") UUID id, @RequestBody DataRapportDto dataRapportDto) throws Exception {

        Optional<Visite> opVisite = visiteDao.findById(id);
        if (opVisite.isPresent() && opVisite.get().getStatut() == 4) {
            Visite visite = opVisite.get();
            if(visite.isContreVisite()){
               log.info("checking confirmity for for viiste staus 4 and visite type control visite ");
            }
            else{
                log.info("checking confirmity for for viiste staus 4 and visite type control initial "); 
            }
            Ligne ligne = visite.getInspection().getLigne();
            if(ligne == null){
                log.info("could not find visite  inspection ligne data");
            }
            Map<String, String> resultvDtos = rapportDeVisiteDao.getResultVisiteIdMap(id);
            System.out.println("size of rapport machine " + resultvDtos.size());
            System.out.println("");
            log.info("setting visite is confirm to 1");
            visite.setIsConform(1);
            dataRapportDto.getDatas().forEach(catTest -> {
                modelDao.getModelWithLigneAndIdCat(ligne, catTest.getId_test())
                        .ifPresent(constructor -> {
                            Map<String, String> data = rapportResult(catTest.getFilename(), constructor);
                            data.forEach((key, value)-> {
                                value = Double.valueOf(value).toString();
                                if(!resultvDtos.containsKey(key) || !value.contains(resultvDtos.get(key)))
                                log.info("setting viiste is confirm to 2");
                                    visite.setIsConform(2);
                            });
                        });
            });

            int status = visite.getStatut();
            boolean isConform = visite.getIsConform() == 1;
            visite.setStatut(isConform ? status+2 : status+1);
            log.info("updating/saving viiste");
            visiteDao.save(visite);
            log.info("sending notification service");
            scheduledService.notifyFraudService(!isConform, isConform ? "" : "FR001", visite);
            scheduledService.notifyCaisseForModification(visite);
            return new ResponseEntity<>(new conformityResponse(isConform), HttpStatus.OK);

        }

        throw new ExceptionResponse("visite id not found");
    }

    private Map<String, String> rapportResult(String filename, ConstructorModel constructor) {
        Map<String, String> data = new HashMap<>();
        switch (constructor.getName()) {
            case "Autovision":
                data = autovisionRapport.start(filename);
                log.info("constructor name is Autovision");
                break;
            case "Bosch":
                data = autovisionRapport.start(filename);
                log.info("constructor name is Bosch");
                break;
            case "Capelec":
                data = autovisionRapport.start(filename);
                log.info("constructor name is Capelec");
                break;
            case "Ravaglioli":
                data = autovisionRapport.start(filename);
                log.info("constructor name is Ravaglioli");
                break;
            case "SatelliteNgono":
                data = autovisionRapport.start(filename);
                log.info("constructor name is SatelliteNgono");
                break;
            case "EuroSystem":
                data = autovisionRapport.start(filename);
                log.info("constructor name is EuroSystem");
                break;
        }

        return data;
    }
}