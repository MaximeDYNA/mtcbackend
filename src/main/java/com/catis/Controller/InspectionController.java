package com.catis.Controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.catis.objectTemporaire.UserInfoIn;
import com.catis.service.*;
import org.apache.commons.codec.binary.Base64;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.catis.Controller.message.Message;
import com.catis.model.Inspection;
import com.catis.model.Visite;
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

    @PostMapping(value = "/api/v1/inspections")
    public ResponseEntity<Object> ajouterInspection(@RequestBody InpectionReceived inspectionReceived) throws IOException {


        LOGGER.trace("Nouvelle inpection...");
        Inspection inspection = new Inspection(inspectionReceived);
        inspection.setControleur(controleurService.findControleurBykeycloakId(inspectionReceived.getControleurId()));
        inspection.setLigne(ligneService.findLigneById(inspectionReceived.getLigneId()));
        inspection.setProduit(produitService.findById(inspectionReceived.getProduitId()));
        inspection.setOrganisation(os.findByOrganisationId(Long.valueOf(UserInfoIn.getUserInfo(request).getOrganisanionId())));
        Visite visite = visiteService.findById(inspectionReceived.getVisiteId());
        inspection.setVisite(visite);
        visiteService.commencerInspection(inspectionReceived.getVisiteId());

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

    @RequestMapping(value = "/api/v1/upload/signature", method = RequestMethod.POST)
    public ResponseEntity<Object> uploadImage2(@RequestBody SignatureDTO signatureDTO) throws IOException {


            byte[] decoded = Base64.decodeBase64(signatureDTO.getImageValue().split(",")[1]);
            File f= new File(env.getProperty("signature.disk.path"));
            if(!f.exists())
                f.mkdirs();

            String filePath = env.getProperty("signature.disk.path") + signatureDTO.getVisiteId() + ".png";

            Path path = Paths.get(filePath);
            System.out.println("*******************" + filePath);

            FileOutputStream fos = new FileOutputStream(path.toString());
            fos.write(decoded);
            fos.close();

            Inspection inspection = inspectionService.setSignature(signatureDTO.getVisiteId(), signatureDTO.getVisiteId() + ".png");

            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", inspection);

          /*  try {} catch (Exception e) {
            e.printStackTrace();
            return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, true, "Failed", null);
        }*/

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

    @DeleteMapping("/api/v1/inspections/{id}")
    public ResponseEntity<Object> deleteInspection(@PathVariable Long id) {


            LOGGER.trace("création onglet demandé...");

            inspectionService.deleteInspection(id);
            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "Suppression OK sur" + "Inspection", null);
        /*try { } catch (Exception e) {
            return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, "Erreur " + "Inspection", null);
        }*/

    }
}
