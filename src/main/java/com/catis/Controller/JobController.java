package com.catis.Controller;

import com.catis.model.entity.FraudeType;
import com.catis.model.entity.Intervenant_fraudeType;
import com.catis.model.entity.Visite;
import com.catis.objectTemporaire.FraudeJobPOJO;
import com.catis.repository.FraudeTypeRepository;
import com.catis.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;

@RestController
@CrossOrigin
public class JobController {
    @Autowired
    private FraudeTypeRepository fraudeTypeRepository;
    @Autowired
    private VisiteService vs;
    @Autowired
    private ProduitService ps;
    @Autowired
    private GieglanFileService gieglanFileService;
    @Autowired
    private CategorieTestVehiculeService catSer;
    @RequestMapping(method=RequestMethod.POST, value="/public/fraudes")
    public ResponseEntity<Object> isThereAfraud(@RequestBody FraudeJobPOJO fraudeJobPOJO) throws Exception {
        if(fraudeJobPOJO.getCode() == null){
            Optional<FraudeType> fraudeType = fraudeTypeRepository.findByCodeAndActiveStatusTrue(fraudeJobPOJO.getCode());
            Set<Intervenant_fraudeType> intervenant_fraudeTypes;
            if(fraudeType.isPresent())
                intervenant_fraudeTypes = fraudeType.get().getIntervenant_fraudeTypes();
            else
                throw new Exception("Code fraude erroné");
            Visite visite = vs.findById(fraudeJobPOJO.getVisiteId());
            double score;
            for(Intervenant_fraudeType intervenant_fraudeType : intervenant_fraudeTypes){

                switch (intervenant_fraudeType.getIntervenantFraude().getName()) {

                    case "Controleur":
                        if(!fraudeJobPOJO.isFraud()){

                            //mise a jour du score en enlevant la valeur de la depreciation au score actuelle
                            //et en gardant l'historique de la modification su solde apres
                            score =  (visite.getInspection().getControleur().getScore() + intervenant_fraudeType.getAppreciation());

                        }else{
                            score =  (visite.getInspection().getControleur().getScore() - intervenant_fraudeType.getDepreciation());
                        }
                        // setscore in table controleur
                        visite.getInspection().getControleur().setScore(score);
                        break;
                    case "ProprietaireVehicule":
                        if(!fraudeJobPOJO.isFraud()){

                            //mise a jour du score en enlevant la valeur de la depreciation au score actuelle
                            //et en gardant l'historique de la modification su solde apres
                            score =  (visite.getCarteGrise().getProprietaireVehicule().getScore() + intervenant_fraudeType.getAppreciation());

                        }else{
                            score =  (visite.getCarteGrise().getProprietaireVehicule().getScore() - intervenant_fraudeType.getDepreciation());
                        }
                        // setscore in table controleur
                        visite.getCarteGrise().getProprietaireVehicule().setScore(score);
                        break;
                    case "Organisation":
                        if(!fraudeJobPOJO.isFraud()){

                            //mise a jour du score en enlevant la valeur de la depreciation au score actuelle
                            //et en gardant l'historique de la modification su solde apres
                            score =  (visite.getOrganisation().getScore() + intervenant_fraudeType.getAppreciation());

                        }else{
                            score =  (visite.getOrganisation().getScore() - intervenant_fraudeType.getDepreciation());
                        }
                        // setscore in table controleur
                        visite.getOrganisation().setScore(score);
                        break;
                    case "Vehicule":
                        if(!fraudeJobPOJO.isFraud()){

                            //mise a jour du score en enlevant la valeur de la depreciation au score actuelle
                            //et en gardant l'historique de la modification su solde apres
                            score =  (visite.getCarteGrise().getVehicule().getScore() + intervenant_fraudeType.getAppreciation());

                        }else{
                            score =  (visite.getCarteGrise().getVehicule().getScore() - intervenant_fraudeType.getDepreciation());
                        }
                        // setscore in table controleur
                        visite.getOrganisation().setScore(score);
                        break;


                }



            }
            visite = vs.add(visite);
            return ApiResponseHandler.generateResponse(HttpStatus.OK,
                    true, "success"+ "infos fraudes", visite);
        }

        return ApiResponseHandler.generateResponse(HttpStatus.OK,
                true, "Erreur", null);






    }

    @GetMapping("/public/maj/{id}")
    public void majvisiteEvent(@PathVariable Long id){
        try {
            VisiteController.dispatchEdit(vs.findById(id),
                    vs, gieglanFileService, catSer, ps);
            System.out.println("le Job a effectué un chanqement sur la visite n°"+id+" :)");

        }
        catch (Exception e) {
            System.out.println("Erreur survenur lors de la notification du Job");
        }
    }
}
