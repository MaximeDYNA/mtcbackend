package com.catis.controller;

import com.catis.controller.message.Message;
import com.catis.model.entity.*;
import com.catis.objectTemporaire.CaissierDTO;
import com.catis.objectTemporaire.CaissierPOJO;
import com.catis.service.CaisseService;
import com.catis.service.CaissierService;
import com.catis.service.OrganisationService;
import com.catis.service.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.*;

@RestController
@CrossOrigin
@RequestMapping("api/v1/admin/caissiers")
public class CaissierController {

    @Autowired
    private CaissierService caissierService;
    @Autowired
    private CaisseService caisseService;
    @Autowired
    private OrganisationService organisationService;
    @Autowired
    private UtilisateurService us;

    @PostMapping
    public ResponseEntity<Object> enregistrer(@RequestBody CaissierPOJO caissierPOJO){
        Caissier caissier = new Caissier();
        Date date = caissierPOJO.getDateNaiss() == null ? null:
                new Date(caissierPOJO.getDateNaiss());
        Caisse caisse = caissierPOJO.getCaisse() == null? null : caisseService.findCaisseById(caissierPOJO.getCaisse());
        Utilisateur utilisateur = caissierPOJO.getUser() == null? null : us.findUtilisateurById(caissierPOJO.getUser());
        Organisation organisation = caissierPOJO.getOrganisationId()==null ? null : organisationService.findByOrganisationId(caissierPOJO.getOrganisationId());

        Partenaire partenaire = new Partenaire(caissierPOJO);
        partenaire.setOrganisation(organisation);
        partenaire.setDateNaiss(date);
        caissier.setPartenaire(partenaire);
        caissier.setUser(utilisateur);
        caissier.setCaisse(caisse);
        caissier.setOrganisation(organisation);

        if(caissierPOJO.getCaisse() != null)
        caissierService.findByCaisse(caissierPOJO.getCaisse()).forEach(
                caissier1 -> caissier1.setCaisse(null)
        );

        try {
            caissier = caissierService.add(caissier);
        } catch (SQLIntegrityConstraintViolationException e) {
            e.printStackTrace();
            return ApiResponseHandler.generateResponse(HttpStatus.OK, false, "Duplicata de champs",
                    caissier);
        }

        return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success",
                caissier);
    }
    @GetMapping
    public ResponseEntity<Object> list(){
        List<Caissier> caissiers = caissierService.findAll();
        List<CaissierDTO> caissierDTOs = new ArrayList<>();
        CaissierDTO caissierDTO ;

        for(Caissier c : caissiers){
            caissierDTO = new CaissierDTO();
            caissierDTO.setCaisse(c.getCaisse()==null?null:c.getCaisse().getCaisse_id());
            caissierDTO.setCaissierId(c.getCaissierId());
            caissierDTO.setCni(c.getPartenaire()==null ? null : c.getPartenaire().getCni());
            if(c.getPartenaire() != null){
                caissierDTO.setNom(c.getPartenaire().getNom());
                caissierDTO.setPrenom(c.getPartenaire().getPrenom());
                caissierDTO.setDateNaiss(c.getPartenaire().getDateNaiss());
                caissierDTO.setEmail(c.getPartenaire().getEmail());
                caissierDTO.setLieuDeNaiss(c.getPartenaire().getLieuDeNaiss());
                caissierDTO.setOrganisation(c.getOrganisation());
                caissierDTO.setPassport(c.getPartenaire().getPassport());
                caissierDTO.setPermiDeConduire(c.getPartenaire().getPermiDeConduire());
                caissierDTO.setTelephone(c.getPartenaire().getTelephone());
                caissierDTO.setCreatedDate(c.getPartenaire().getCreatedDate());
            }
            caissierDTOs.add(caissierDTO);
        }


        return ApiResponseHandler.generateResponse(HttpStatus.OK,
                true, Message.ListOK + " Users", caissierDTOs);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id){
        try {
            caissierService.deleteById(id);
            return ApiResponseHandler.generateResponse(HttpStatus.OK,
                    true, "OK", null);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponseHandler.generateResponse(HttpStatus.OK,
                    true, "KO", null);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value="/api/v1/admin/caissiers/select")
    public ResponseEntity<Object> getLexiquesOfMtcforSelect(){

        List<Caissier> cats = caissierService.findAll();
        List<Map<String, String>> catsSelect = new ArrayList<>();

        Map<String, String> cat;

        for(Caissier c: cats){
            cat = new HashMap<>();
            cat.put("id", String.valueOf(c.getCaissierId()));
            cat.put("name", c.getPartenaire().getNom()+" "+ c.getPartenaire().getPrenom() +" | "
                    + (c.getOrganisation() == null? "Tous" : c.getOrganisation().getNom()));
            catsSelect.add(cat);
        }

        return ApiResponseHandler.generateResponse(HttpStatus.OK,
                true, "OK", catsSelect);
    }
}