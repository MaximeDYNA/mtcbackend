package com.catis.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

import com.catis.controller.configuration.SessionData;
import com.catis.controller.exception.WrongConfigurationException;
import com.catis.model.entity.Utilisateur;
import com.catis.objectTemporaire.UserDTO;
import com.catis.objectTemporaire.UserInfoIn;
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

import com.catis.controller.message.Message;
import com.catis.model.entity.Inspection;
import com.catis.model.entity.Visite;
import com.catis.objectTemporaire.InpectionReceived;
import com.catis.objectTemporaire.SignatureDTO;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin
public class InspectionController {

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


    private static Logger LOGGER = LoggerFactory.getLogger(InspectionController.class);

    @PostMapping(value = "/api/v1/controleur/inspections")
    @Transactional
    public ResponseEntity<Object> ajouterInspection(@RequestBody InpectionReceived inspectionReceived) throws Exception {


        LOGGER.trace("Nouvelle inpection...");

        Inspection inspection = new Inspection(inspectionReceived);
        inspection.setControleur(controleurService.findControleurBykeycloakId(inspectionReceived.getControleurId()));
        inspection.setLigne(ligneService.findLigneById(inspectionReceived.getLigneId()));
        inspection.setProduit(produitService.findById(inspectionReceived.getProduitId()));
        inspection.setOrganisation(os.findByOrganisationId(Long.valueOf(UserInfoIn.getUserInfo(request).getOrganisanionId())));
        inspection.setDateDebut(new Date());
        Visite visite = visiteService.findById(inspectionReceived.getVisiteId());
        if(visite.getInspection() != null)
            throw new Exception("Une inspection est déjà en cours pour cette visite");
        inspection.setVisite(visite);
        visiteService.commencerInspection(visite);

        inspection = inspectionService.addInspection(inspection);



        this.gieglanFileService.createFileGieglanOfCgrise(visite.getCarteGrise(), inspection);
				
				/*String[] result = "this is a test".split("\\s");
			     for (int x=0; x<result.length; x++)
			         System.out.println(result[x]);*/
        return ApiResponseHandler.generateResponse(HttpStatus.OK, true, Message.OK_ADD + "Inspection", inspection);
			/*try {}
			catch (Exception e) {
				return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, Message.ERREUR_ADD + "Inspection", null);
			}*/

    }

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
            }finally {
                stream.close();
            }



            UserDTO userDTO = UserInfoIn.getUserInfo(request);
            Utilisateur u =utilisateurService.findUtilisateurByKeycloakId(userDTO.getId());

            Inspection inspection = inspectionService
                    .setSignature(signatureDTO.getVisiteId(), signatureDTO.getVisiteId()
                            + ".png", u.getControleur());

            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", inspection);



    }


    @GetMapping(value = "/api/v1/inspections")
    public ResponseEntity<Object> inspectionList() {

        try {
            LOGGER.trace("création onglet demandé...");


            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, Message.OK_LIST_VIEW + "Inspection", inspectionService.findAllInspection());
        } catch (Exception e) {
            return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, Message.ERREUR_LIST_VIEW + "Inspection", null);
        }

    }

    @DeleteMapping("/api/v1/admin/inspections/{id}")
    public ResponseEntity<Object> deleteInspection(@PathVariable Long id) {


            LOGGER.trace("création onglet demandé...");

            inspectionService.deleteInspection(id);
            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "Suppression OK sur" + "Inspection", null);
        /*try { } catch (Exception e) {
            return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, "Erreur " + "Inspection", null);
        }*/

    }
}
