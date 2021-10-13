package com.catis.controller;

import com.catis.model.entity.Controleur;
import com.catis.model.entity.FraudeType;
import com.catis.model.entity.Intervenant_fraudeType;
import com.catis.model.entity.Visite;
import com.catis.objectTemporaire.FraudeJobPOJO;
import com.catis.objectTemporaire.ProprietaireDTO;
import com.catis.repository.FraudeTypeRepository;
import com.catis.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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
    @Autowired
    private ControleurService controleurService;

    @GetMapping(value = "/public/controleurs/{organisationId}")
    public ResponseEntity<Object> getControleurOfOrganisation(@PathVariable Long organisationId) {

        List<Controleur> controleurs = controleurService.findAllByOrganisation(organisationId);
        List<ProprietaireDTO> ps = new ArrayList<>();
        ProprietaireDTO pro;
        for(Controleur p : controleurs){
            pro = new ProprietaireDTO();
            pro.setIdControleur(p.getIdControleur());
            pro.setNom(p.getPartenaire().getNom());
            pro.setPrenom(p.getPartenaire().getPrenom());
            pro.setDateNaiss(p.getPartenaire().getDateNaiss());
            pro.setEmail(p.getPartenaire().getEmail());
            pro.setLieuDeNaiss(p.getPartenaire().getLieuDeNaiss());
            pro.setOrganisation(p.getOrganisation());
            pro.setPassport(p.getPartenaire().getPassport());
            pro.setPermiDeConduire(p.getPartenaire().getPermiDeConduire());
            pro.setTelephone(p.getPartenaire().getTelephone());
            pro.setCreatedDate(p.getPartenaire().getCreatedDate());
            pro.setCni(p.getPartenaire().getCni());
            pro.setAgremment(p.getAgremment());
            pro.setLogin(p.getUtilisateur().getLogin());
            pro.setPartenaireId(p.getPartenaire().getPartenaireId());
            ps.add(pro);
        }
        return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", ps);
    }

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

            Visite visite = vs.findById(id);
        try {
            vs.dispatchEdit(visite);
            System.out.println("le Job a effectué un chanqement sur la visite n°"+id+", le statut de la visite est "+visite.getStatut() +" :)");
        }
        catch (Exception e) {
            System.err.println("Erreur survenur lors de la notification du Job");
        }
    }

}
