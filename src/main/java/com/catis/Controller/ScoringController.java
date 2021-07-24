package com.catis.Controller;

import com.catis.Controller.exception.CodeAlreadyExistException;
import com.catis.model.entity.FraudeType;
import com.catis.model.entity.IntervenantFraude;
import com.catis.model.entity.Intervenant_fraudeType;
import com.catis.model.entity.Visite;
import com.catis.objectTemporaire.FraudeJobPOJO;
import com.catis.objectTemporaire.FraudePOJO;
import com.catis.objectTemporaire.Intervenant_fraudeTypePOJO;
import com.catis.objectTemporaire.ObjectForSelect;
import com.catis.repository.FraudeTypeRepository;
import com.catis.repository.IntervenantFraudeRepository;
import com.catis.repository.Intervenant_fraudeTypeRepository;
import com.catis.service.VisiteService;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/admin/score")
public class ScoringController {

    @Autowired
    private IntervenantFraudeRepository intervenantFraudeRepository;
    @Autowired
    private FraudeTypeRepository fraudeTypeRepository;
    @Autowired
    private Intervenant_fraudeTypeRepository iftr;
    @Autowired
    private VisiteService visiteService;

    @GetMapping("/intervenants")
    public ResponseEntity<Object> getIntervenants(){
        List<IntervenantFraude> intervenantFraudes = intervenantFraudeRepository.findByActiveStatusTrue();
        List<Map<String, String>> catsSelect = new ArrayList<>();

        Map<String, String> cat;
        for(IntervenantFraude c: intervenantFraudes){
            cat = new HashMap<>();
            cat.put("id", String.valueOf(c.getId()));
            cat.put("name", c.getName() );
            catsSelect.add(cat);
        }
        return ApiResponseHandler.generateResponse(HttpStatus.OK,true,"OK", catsSelect);

    }

    @GetMapping("/fraudes")
    public ResponseEntity<Object> getfraudes(){
        List<FraudeType> fraudeTypes = fraudeTypeRepository.findByActiveStatusTrue();
        List<Map<String, String>> catsSelect = new ArrayList<>();
        Map<String, String> cat;

        for(FraudeType c: fraudeTypes){
            cat = new HashMap<>();
            cat.put("id", String.valueOf(c.getId()));
            cat.put("name", c.getCode() );
            cat.put("description", c.getDescription());
            catsSelect.add(cat);
        }
        return ApiResponseHandler.generateResponse(HttpStatus.OK,true,"OK", catsSelect);
    }

    @PostMapping("/fraudes")
    public ResponseEntity<Object> savefraude(@RequestBody FraudePOJO fraudePOJO){

        FraudeType fraude = new FraudeType();
        fraude.setCode(fraudePOJO.getCode());
        fraude.setDescription(fraudePOJO.getDescription());
        try {
            if(isThisCodeExist(fraudePOJO.getCode()))
                throw new CodeAlreadyExistException("ce code existe déjà");
        }catch (CodeAlreadyExistException c){
            return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,false,"Le code existe déjà", null);
        }

        fraude = fraudeTypeRepository.save(fraude);

        return ApiResponseHandler.generateResponse(HttpStatus.OK,true,"OK", fraude);
    }

    @DeleteMapping("/fraudes/{id}")
    public ResponseEntity<Object> deleteFraude(@PathVariable Long id) {

            fraudeTypeRepository.deleteById(id);

        return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "OK", null );
    }

    @GetMapping("/regles/{id}")
    public ResponseEntity<Object> getRules(@PathVariable Long id){

        List<Intervenant_fraudeType> intervenant_fraudeTypes = iftr.findByFraudeType_IdAndActiveStatusTrue(id);
        List<Map<String, Object>> catsSelect = new ArrayList<>();

        Map<String, Object> cat;

        for(Intervenant_fraudeType c: intervenant_fraudeTypes){
            cat = new HashMap<>();
            cat.put("id", String.valueOf(c.getId()));
            cat.put("appreciation", String.valueOf(c.getAppreciation()) );
            cat.put("depreciation", String.valueOf(c.getDepreciation()) );
            cat.put("fraudeType", new ObjectForSelect(c.getFraudeType().getId(), c.getFraudeType().getCode()) );
            cat.put("intervenantFraude", String.valueOf(c.getIntervenantFraude().getName()));
            catsSelect.add(cat);
        }


        return ApiResponseHandler.generateResponse(HttpStatus.OK,true,"OK", catsSelect);
    }
    @DeleteMapping("/regles/{id}")
    public ResponseEntity<Object> delRule(@PathVariable Long id){

            iftr.deleteById(id);

        return ApiResponseHandler.generateResponse(HttpStatus.OK,true,"OK", null);
    }
    @RequestMapping(method=RequestMethod.POST, value="api/v1/fraudes")
    public ResponseEntity<Object> isThereAfraud(@RequestBody FraudeJobPOJO fraudeJobPOJO) throws Exception {
        Optional<FraudeType> fraudeType = fraudeTypeRepository.findByCodeAndActiveStatusTrue(fraudeJobPOJO.getCode());
        Set<Intervenant_fraudeType> intervenant_fraudeTypes;
        if(fraudeType.isPresent())
            intervenant_fraudeTypes = fraudeType.get().getIntervenant_fraudeTypes();
        else
            throw new Exception("Code fraude erroné");


        Visite visite = visiteService.findById(fraudeJobPOJO.getVisiteId());
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

        visite = visiteService.add(visite);


        return ApiResponseHandler.generateResponse(HttpStatus.OK,
                true, "success"+ "infos fraudes", visite);

    }



    @PostMapping("/regles")
    public ResponseEntity<Object> setIntervenantFraud(@RequestBody Intervenant_fraudeTypePOJO intervenant_fraudeType){
        System.out.println("Received data "+ ToStringBuilder.reflectionToString(intervenant_fraudeType));
        Intervenant_fraudeType t = new Intervenant_fraudeType();
        Optional<FraudeType> ift = fraudeTypeRepository.findByCodeAndActiveStatusTrue(intervenant_fraudeType.getFraudeType().getName());
        Optional<IntervenantFraude> ifr = intervenantFraudeRepository.findById(Long.valueOf(intervenant_fraudeType.getIntervenantFraude().getId()));
        t.setFraudeType(ift.get());
        t.setAppreciation(Double.valueOf(intervenant_fraudeType.getAppreciation()));
        t.setDepreciation(Double.valueOf(intervenant_fraudeType.getDepreciation()));
        t.setIntervenantFraude(ifr.get());
        t.setId(intervenant_fraudeType.getId());
        t = iftr.save(t);
        Map<String, String> cat;
        cat = new HashMap<>();
        cat.put("id", String.valueOf(t.getId()));
        cat.put("appreciation", String.valueOf(t.getAppreciation()));
        cat.put("depreciation", String.valueOf(t.getDepreciation()));
        cat.put("fraudeType", t.getFraudeType().getCode());
        cat.put("intervenantFraude", String.valueOf(t.getIntervenantFraude().getName()));

        return ApiResponseHandler.generateResponse(HttpStatus.OK,true,"OK", cat);
    }

    @DeleteMapping("/fraudes/intervenants/{id}")
    public ResponseEntity<Object> delIntervenantFraud(@PathVariable Long id){

        iftr.deleteById(id);

        return ApiResponseHandler.generateResponse(HttpStatus.OK,true,"OK", null);
    }

    public boolean isThisCodeExist(String code){
        return fraudeTypeRepository.findByCodeAndActiveStatusTrue(code).isPresent();
    }
}
