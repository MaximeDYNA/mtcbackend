package com.catis.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.UUID;
import java.util.List;
import java.util.Set;

import com.catis.controller.configuration.SessionData;
import com.catis.controller.exception.WrongConfigurationException;
import com.catis.model.entity.Message;
import com.catis.model.entity.Utilisateur;
import com.catis.objectTemporaire.UserDTO;
import com.catis.objectTemporaire.UserInfoIn;
import com.catis.repository.GieglanFileRepository;
import com.catis.repository.InspectionRepository;
import com.catis.repository.MessageRepository;
import com.catis.repository.VisiteRepository;
import com.catis.service.*;
import org.apache.commons.codec.binary.Base64;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import com.catis.model.control.GieglanFile;
import com.catis.model.entity.Inspection;
import com.catis.model.entity.Visite;
import com.catis.objectTemporaire.InpectionReceived;
import com.catis.objectTemporaire.SignatureDTO;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin
public class InspectionController {

    @Autowired
    private VisiteRepository visiteRepository;

    @Autowired
    HttpServletRequest request;
    @Autowired
    private InspectionService inspectionService;
    @Autowired
    private LigneService ligneService;
    @Autowired
    private ControleurService controleurService;
    @Autowired
    private UtilisateurService utilisateurService;
    @Autowired
    private ProduitService produitService;
    @Autowired
    private VisiteService visiteService;
    @Autowired
    private GieglanFileService gieglanFileService;
    @Autowired
    private OrganisationService os;
    @Autowired
    private Environment env;
    @Autowired
    private MessageRepository msgRepo;

    @Autowired
    private GieglanFileRepository gieglanFileRepository;

    @Autowired
    private InspectionRepository inspectionRepository;



    private static Logger LOGGER = LoggerFactory.getLogger(InspectionController.class);


    @DeleteMapping(value="/api/v1/controleur/{inspectionId}")
     @Transactional
    public ResponseEntity<Object> revertInspection(@PathVariable UUID inspectionId) {
        try {

            Visite visite = visiteRepository.findByInspection_IdInspection(inspectionId);
            if(visite == null) {
                LOGGER.info("NO VISITE FOUND WITH THAT INSPCETION...");
            }
            if(visite != null) {
                // Detach Visite from the Inspection
                LOGGER.info("VISITE FOUND WITH THAT INSPCETION...");
                visite.setInspection(null);
                LOGGER.info("INSPECTION RESET DONE");
                visite.resetState(); // Reset state to pending
                LOGGER.info("VISITE STATE RESET DONE");
                visite.clearProcessAndRapports(); // Clear process and rapports
                visite.setStatut(1);
                LOGGER.info("VISITE RAPPORTS RESET DONE");
                visiteRepository.save(visite);
                LOGGER.info("VISITE UPDATE DONE");
            }
            Inspection inspection = inspectionRepository.findById(inspectionId).orElse(null);
            // Inspection inspection = inspectionRepository.findById(inspectionId).orElseThrow(() -> new RuntimeException("Inspection not found"));
            if (inspection == null) {
                LOGGER.info("NO INSPECTION FOUND WITH THAT ID...");
            } else {
                LOGGER.info("INSPECTION FOUND WITH THAT ID...");
                inspection.setVisite(null);
                LOGGER.info("VISITE RESET DONE");
                inspectionRepository.save(inspection);
                LOGGER.info("INSPECTION UPDATE DONE");
            }
            Message msg = msgRepo.findByCode("IP001");
            return ApiResponseHandler.generateResponseWithAlertLevel(HttpStatus.OK, true, msg, visite);

        } catch (Exception e) {
            Message msg = msgRepo.findByCode("IP002");
            return ApiResponseHandler.generateResponseWithAlertLevel(HttpStatus.OK, false, msg, e.getMessage());
        }
    }


    @PostMapping(value = "/api/v1/controleur/inspections")
    @Transactional
    public ResponseEntity<Object> ajouterInspection(@RequestBody InpectionReceived inspectionReceived) throws Exception {

        try {

            LOGGER.info("ADD INSPECTION REQUEST...");

            Inspection inspection = new Inspection(inspectionReceived);
            inspection.setControleur(controleurService.findControleurBykeycloakId(inspectionReceived.getControleurId()));
            inspection.setLigne(ligneService.findLigneById(inspectionReceived.getLigneId()));
            inspection.setProduit(produitService.findById(inspectionReceived.getProduitId()));
            inspection.setOrganisation(os.findByOrganisationId(UserInfoIn.getUserInfo(request).getOrganisanionId()));
            inspection.setDateDebut(new Date());
            Visite visite = visiteService.findById(inspectionReceived.getVisiteId());
            if(visite.getInspection() != null)
                throw new Exception("Une inspection est déjà en cours pour cette visite");
            inspection.setVisite(visite);
            visite.setInspection(inspection);
            
            visite = visiteService.commencerInspection(visite);
            
            // flemming added this conditional statement
            if(visite.isContreVisite()){
                LOGGER.info("contreVisite, resetting is_accepted of visuel('.json' file) gieglan to 0");
                this.gieglanFileService.updateGieglanFileStatus(visite.getControl().getId(), visite.getIdVisite());
                // this.gieglanFileService.updateGieglanFileIsAcceptByInspectionId(visite.getInspection().getIdInspection());
            }
            //inspection = inspectionService.addInspection(inspection);



            this.gieglanFileService.createFileGieglanOfCgrise(visite.getCarteGrise(), visite.getInspection());

            LOGGER.info("Recherche carte grise reussi");
            Message msg = msgRepo.findByCode("IP001");

            return ApiResponseHandler.generateResponseWithAlertLevel(HttpStatus.OK, true, msg, inspection);
        }
        catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("ERROR WHEN RECORDING INSPECTION");
            Message msg = msgRepo.findByCode("IP002");
            return ApiResponseHandler.generateResponseWithAlertLevel(HttpStatus.OK, false, msg, e.getMessage());
        }

    }

    @Transactional
    @PostMapping(value = "/api/v1/controleur/upload/signature")
    public ResponseEntity<Object> uploadImage2(@RequestBody SignatureDTO signatureDTO) throws Exception {

            String diskPathToSignature = env.getProperty("signature.disk.path");


            byte[] decoded = Base64.decodeBase64(signatureDTO.getImageValue().split(",")[1]);
            if(diskPathToSignature == null)
                throw new WrongConfigurationException("Fill signature.disk.path in application property");
            File f= new File(diskPathToSignature );

                if(!f.exists())
                    f.mkdirs();


            String filePath = diskPathToSignature + signatureDTO.getVisiteId() + ".png";

            OutputStream stream = null;
            Path path = Paths.get(filePath);
            try {
                stream = new FileOutputStream(path.toString());
                stream.write(decoded);
            }
            catch (Exception e){
                LOGGER.error("Error when creating signature");
                Message msg = msgRepo.findByCode("IP004");
                return ApiResponseHandler.generateResponseWithAlertLevel(HttpStatus.OK, false, msg, null);


            }finally {
                stream.close();
            }



            UserDTO userDTO = UserInfoIn.getUserInfo(request);
            Utilisateur u =utilisateurService.findUtilisateurByKeycloakId(userDTO.getId());

            Inspection inspection = inspectionService
                    .setSignature(signatureDTO.getVisiteId(), signatureDTO.getVisiteId()
                            + ".png", u.getControleur());
                            
            Message msg = msgRepo.findByCode("IP003");

            return ApiResponseHandler.generateResponseWithAlertLevel(HttpStatus.OK, true, msg, inspection);



    }


    @GetMapping(value = "/api/v1/inspections")
    public ResponseEntity<Object> inspectionList() {

        try {
            List<Inspection> inspectionList = inspectionService.findAllInspection();
            LOGGER.info("INSPECTION LIST");
            Message msg = msgRepo.findByCode("IP005");
            return ApiResponseHandler.generateResponseWithAlertLevel(HttpStatus.OK, true, msg, inspectionList);
        } catch (Exception e) {
            e.printStackTrace();
            Message msg = msgRepo.findByCode("IP006");
            return ApiResponseHandler.generateResponseWithAlertLevel(HttpStatus.OK, false, msg, null);
        }

    }



    @DeleteMapping("/api/v1/admin/inspections/{id}")
    public ResponseEntity<Object> deleteInspection(@PathVariable UUID id) {
        try {
            LOGGER.info("INSPECTION DELETED");
            Message msg = msgRepo.findByCode("IP007");
            inspectionService.deleteInspection(id);
            return ApiResponseHandler.generateResponseWithAlertLevel(HttpStatus.OK, true, msg, null);
         } catch (Exception e) {
            Message msg = msgRepo.findByCode("IP008");
            return ApiResponseHandler.generateResponseWithAlertLevel(HttpStatus.OK, false, msg, null);
        }

    }
}
