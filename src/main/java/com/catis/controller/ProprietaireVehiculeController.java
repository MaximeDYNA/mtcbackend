package com.catis.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.catis.model.entity.Organisation;
import com.catis.objectTemporaire.ProprietaireDTO;
import com.catis.objectTemporaire.ProprietairePOJO;
import com.catis.objectTemporaire.UserInfoIn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.catis.controller.message.Message;
import com.catis.model.entity.Partenaire;
import com.catis.model.entity.ProprietaireVehicule;
import com.catis.objectTemporaire.ClientPartenaire;
import com.catis.service.OrganisationService;
import com.catis.service.PartenaireService;
import com.catis.service.ProprietaireVehiculeService;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin
public class ProprietaireVehiculeController {

    @Autowired
    private ProprietaireVehiculeService proprietaireVehiculeadresseService;
    @Autowired
    private PartenaireService partenaireService;
    @Autowired
    private OrganisationService os;
    @Autowired
    HttpServletRequest request;

    private static Logger LOGGER = LoggerFactory.getLogger(ProprietaireVehiculeController.class);

    @GetMapping("/api/v1/all/search/proprietaires/{nom}")
    public ResponseEntity<Object> proprioList(@PathVariable String nom) {
        try {
            LOGGER.trace("List des propriétaires des vehicules...");
            List<Map<String,Object>> maps = new ArrayList<>();
            List<ProprietaireVehicule> proprietaireVehicules = new ArrayList<>();
            proprietaireVehiculeadresseService.searchProprio(nom).forEach(proprietaireVehicules::add);
            Map proprio;
            for(ProprietaireVehicule p : proprietaireVehicules){
                proprio = new HashMap();
                proprio.put("id", p.getProprietaireVehiculeId());
                proprio.put("prenom", p.getPartenaire().getPrenom() == null ? "": p.getPartenaire().getPrenom() );
                proprio.put("nom", p.getPartenaire().getNom());
                maps.add(proprio);
            }
            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "succès"
                    ,maps );
        } catch (Exception e) {
            LOGGER.error("Une erreur est survenu lors de l'accès à la liste des proprietaire");
            return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, "Une erreur est survenu", null);
        }
    }

    @PostMapping("/api/v1/cg/proprietaires")
    public ResponseEntity<Object> addProprio(@RequestBody ClientPartenaire clientPartenaire) throws ParseException {
        try {
            LOGGER.trace("Ajout d'un propriétaire...");
            Organisation organisation =os.findByOrganisationId(Long
                            .valueOf(UserInfoIn
                                    .getUserInfo(request)
                                    .getOrganisanionId()));

            ProprietaireVehicule pv = new ProprietaireVehicule();
            Partenaire partenaire = new Partenaire();
            partenaire.setCni(clientPartenaire.getCni());
            //System.out.println("*******************"+clientPartenaire.getNom());
            if (clientPartenaire.getDateNaiss() != null) {
                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(clientPartenaire.getDateNaiss());
                ;
                partenaire.setDateNaiss(date);
            } else
                partenaire.setDateNaiss(null);


            partenaire.setEmail(clientPartenaire.getEmail());
            partenaire.setTelephone(clientPartenaire.getTelephone());
            partenaire.setNom(clientPartenaire.getNom());
            partenaire.setPrenom(clientPartenaire.getPrenom());
            partenaire.setPassport(clientPartenaire.getPassport());
            partenaire.setLieuDeNaiss(clientPartenaire.getLieuDeNaiss());
            partenaire.setPermiDeConduire(clientPartenaire.getPermiDeConduire());
            partenaire.setOrganisation(organisation);
            pv.setPartenaire(partenaireService.addPartenaire(partenaire));
            pv.setDescription(clientPartenaire.getVariants());
            pv.setOrganisation(organisation);

            LOGGER.trace("Ajout de " + partenaire.getNom() + " réussi");
            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "succès"
                    , proprietaireVehiculeadresseService.addProprietaire(pv));
        } catch (Exception e) {
            LOGGER.error("Une erreur est survenu l'ajout d'un proprietaire");
            return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, Message.ERREUR_ADD + "Propietaire", null);
        }
    }

    /**Admin**/

    @GetMapping("/api/v1/admin/proprietaires")
    public ResponseEntity<Object> proprioAdminList() {
        try {

            LOGGER.trace("List des propriétaires des vehicules...");
            List<ProprietaireVehicule> props = proprietaireVehiculeadresseService.findAll();
            List<ProprietaireDTO> ps = new ArrayList<>();
            ProprietaireDTO pro;
            for(ProprietaireVehicule p : props){
                pro = new ProprietaireDTO();
                pro.setProprietaireVehiculeId(p.getProprietaireVehiculeId());
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
                pro.setDescription(p.getDescription());
                pro.setPartenaireId(p.getPartenaire().getPartenaireId());
                ps.add(pro);
            }
            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "succès"
                    ,ps);
        } catch (Exception e) {
            LOGGER.error("Une erreur est survenu lors de l'accès à la liste des adresses");
            return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, "Une erreur est survenu", null);
        }
    }

    @PostMapping("/api/v1/admin/proprietaires")
    public ResponseEntity<Object> addproprioAdmin(@RequestBody ProprietairePOJO proprietairePOJO) {
        try {

            LOGGER.trace("List des propriétaires des vehicules...");
            ProprietaireVehicule proprio = new ProprietaireVehicule();
            Date date = proprietairePOJO.getDateNaiss() == null ? null:
                    proprietairePOJO.getDateNaiss();
            Organisation organisation = proprietairePOJO.getOrganisationId()==null ? null : os.findByOrganisationId(proprietairePOJO.getOrganisationId().getId());

            Partenaire partenaire = new Partenaire(proprietairePOJO);
            partenaire.setOrganisation(organisation);
            partenaire.setDateNaiss(date);
            proprio.setPartenaire(partenaire);
            proprio.setOrganisation(organisation);
            proprio.setProprietaireVehiculeId(proprietairePOJO.getProprietaireVehiculeId());

            proprio = proprietaireVehiculeadresseService.addProprietaire(proprio);

            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "succès"
                    ,proprio);
        } catch (Exception e) {
            LOGGER.error("Une erreur est survenu lors de l'accès à la liste des adresses");
            return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, "Une erreur est survenu", null);
        }
    }
    @DeleteMapping("/api/v1/admin/proprietaires/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id){
        try {
            proprietaireVehiculeadresseService.deleteById(id);
            return ApiResponseHandler.generateResponse(HttpStatus.OK,
                    true, "OK", null);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponseHandler.generateResponse(HttpStatus.OK,
                    false, "KO", null);
        }
    }

    @GetMapping("/api/v1/admin/proprietaires/select")
    public ResponseEntity<Object> getCaissesOfMtcforSelect(){

        List<ProprietaireVehicule> cats = proprietaireVehiculeadresseService.findAll();
        List<Map<String, String>> catsSelect = new ArrayList<>();

        Map<String, String> cat;

        for(ProprietaireVehicule c: cats){
            cat = new HashMap<>();
            cat.put("id", String.valueOf(c.getProprietaireVehiculeId()));
            cat.put("name", c.getPartenaire().getNom() +" "+ c.getPartenaire().getPrenom() +" | "
                    + (c.getOrganisation() == null? "Toutes" : c.getOrganisation().getNom()));
            catsSelect.add(cat);
        }

        return ApiResponseHandler.generateResponse(HttpStatus.OK,
                true, "Select catégorie produit OK", catsSelect);
    }
}
