package com.catis.Controller;

import java.util.Optional;

import com.catis.model.entity.MesureVisuel;
import com.catis.service.InspectionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.catis.model.entity.Inspection;
import com.catis.model.entity.Lexique;
import com.catis.objectTemporaire.DefectResponse;
import com.catis.repository.InspectionRepository;
import com.catis.repository.LexiqueRepository;
import com.catis.service.MesureVisuelService;

@RestController
@CrossOrigin
public class MesureVisuelController {
    @Autowired
    private InspectionRepository inspectionRepo;
    @Autowired
    private LexiqueRepository lexiqueRepository;
    @Autowired
    private MesureVisuelService mesurevisuelservice;
    @Autowired
    private InspectionService inspectionService;

    public MesureVisuelController(InspectionRepository inspectionRepo,
                                  LexiqueRepository lexiqueRepository, MesureVisuelService mesurevisuelservice) {

        this.inspectionRepo = inspectionRepo;
        this.lexiqueRepository = lexiqueRepository;
        this.mesurevisuelservice = mesurevisuelservice;
    }

    private static Logger LOGGER = LoggerFactory.getLogger(MesureVisuelController.class);

    @PostMapping("/api/v1/mesurevisuel")
    public ResponseEntity<Object> addMesureVisuel(@RequestBody DefectResponse defectrespons) {

        try {
            Optional<Inspection> inspection = this.inspectionRepo.findById(defectrespons.getIdinspection());
            inspection.ifPresent(inspection1 -> {
                defectrespons.getDefectslist().forEach(defectsmodel -> {
                    Optional<Lexique> lexique = this.lexiqueRepository.findById(defectsmodel.getId());
                    lexique.ifPresent(inspection1::addLexique);
                });
                this.inspectionRepo.save(inspection1);
                LOGGER.trace("List des mesures visuelles...List<MesureVisuel> mesurevisuel "
                        + defectrespons.getDefectslist());
                /*
                 * for(DefectsModel h : defectrespons.getDefectslist()) {
                 * LOGGER.trace("List des mesures"+h.getDefect()); }
                 */
            });

            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", null);

        } catch (Exception e) {
            return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, "Une erreur est survenue", null);
        }
    }


    @PostMapping("/api/v1/controleur/signature")
    public ResponseEntity<Object> recordSignature(@RequestBody DefectResponse defectResponse){

        MesureVisuel m = mesurevisuelservice.addSignatureToMesureVisuel(defectResponse);
           // invisible for tab after validation

        Inspection inspection = inspectionService.findInspectionById(defectResponse.getIdinspection());
        inspection.setVisibleToTab(false);
        inspectionRepo.save(inspection);


        return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", m );

        //return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", "Ok 100%");

       /* try { } catch (Exception e) {
            return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, "Une erreur est survenue", null);
        }*/

    }

   /* @PostMapping("/api/v1/datainspection")
    public ResponseEntity<Object> addDataInspection(@RequestBody MesureVisuel mesurevisuel) {

        System.out.println("hello " + mesurevisuel.toString());
//		  LOGGER.trace("List des mesures visuelles...List<MesureVisuel> mesurevisuel "
//		  +mesurevisuel.getImage1()); 
        Inspection i = inspectionRepo.findById(mesurevisuel.getInspection().getIdInspection()).get();
        mesurevisuel.setInspection(i);
        MesureVisuel m = mesurevisuelservice.addDataInspection(mesurevisuel);
        try {

            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", m);
        } catch (Exception e) {
            return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, "Une erreur est survenue", null);

        }
    }*/
}
