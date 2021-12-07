package com.catis.controller;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import com.catis.controller.configuration.SessionData;
import com.catis.model.entity.Organisation;
import com.catis.objectTemporaire.CarteGrisePOJO;
import com.catis.objectTemporaire.UserInfoIn;
import com.catis.service.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.catis.controller.message.Message;
import com.catis.model.entity.CarteGrise;
import com.catis.model.entity.Vehicule;
import com.catis.model.entity.Visite;
import com.catis.objectTemporaire.CarteGriseReceived;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin
public class CarteGriseController {
    @Autowired
    private CarteGriseService cgs;
    @Autowired
    private ProprietaireVehiculeService pvs;
    @Autowired
    private MarqueService ms;
    @Autowired
    private ProduitService ps;
    @Autowired
    private VehiculeService vs;
    @Autowired
    private VisiteService visiteService;
    @Autowired
    private EnergieService energieService;
    @Autowired
    private OrganisationService os;
    @Autowired
    private VehiculeService vehiculeService;
    @Autowired
    HttpServletRequest request;

    private static Logger LOGGER = LoggerFactory.getLogger(CarteGriseController.class);

    @GetMapping("/api/v1/cartegrise/search/{imCha}")
    public ResponseEntity<Object> search(@PathVariable String imCha) {
        LOGGER.trace("Recherche carte grise...");
        try {
            //cgs.findByImmatriculationOuCarteGrise(imCha)
            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", cgs.findBychassis(imCha));
        } catch (Exception e) {
            return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, "Une erreur est survenue", null);

        }
    }

    @GetMapping("/api/v1/search/cartegrise/assurance/{imma}")
    public ResponseEntity<Object> searchForAssurance(@PathVariable String imma) {
        LOGGER.trace("Recherche carte grise...");
        try {
            //cgs.findByImmatriculationOuCarteGrise(imCha)
            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success",
                    cgs.findCartegriseForAssurance(imma));
        } catch (Exception e) {
            return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false,
                    "Une erreur est survenue", null);

        }
    }
    @GetMapping("/api/v1/search/cartegrise/assurance/imma/{imma}")
    public ResponseEntity<Object> searchForAssuranceByImma(@PathVariable String imma) {
        LOGGER.trace("Recherche carte grise...");
        try {
            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success",
                    cgs.findByImmatriculation(imma));
        } catch (Exception e) {
            return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false,
                    "Une erreur est survenue", null);

        }
    }
    @GetMapping("/api/v1/search/cartegrise/assurance/visite/{imma}")
    public ResponseEntity<Object> isCGhasValidVisite(@PathVariable String imma) {
        LOGGER.trace("visite...");
        try {
            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success",
                    cgs.isCarteGriseHasValidVisite(imma));
        } catch (Exception e) {
            return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false,
                    "Une erreur est survenue", null);

        }
    }


    @GetMapping("/api/v1/cg/search/last/{imCha}")
    public ResponseEntity<Object> searchLast(@PathVariable String imCha) {
        LOGGER.trace("Recherche carte grise...");

        return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", cgs.findLastCgBychassis(imCha));
		/*try {	} 
		catch(Exception e){ 
				return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, "Une erreur est survenue", null );*/


    }

    @GetMapping("/api/v1/cg/cartegrises")
    public ResponseEntity<Object> findAll() {
        LOGGER.trace("Recherche carte grise...");
        try {
            //cgs.findByImmatriculationOuCarteGrise(imCha)
            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", cgs.findAll());
        } catch (Exception e) {
            return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, "Une erreur est survenue", null);

        }
    }

    @PostMapping("/api/v1/cg/cartegrise")
    public ResponseEntity<Object> misajour(@RequestBody CarteGriseReceived carteGriseR) throws IOException {
        LOGGER.trace("mise à jour demandé...");
        Long orgId = SessionData.getOrganisationId(request);
        Organisation organisation = os.findByOrganisationId(orgId);
        try {

        CarteGrise carteGrise = new CarteGrise(carteGriseR);
        Vehicule vehicule;
        //initialise le vehicule avec les éléments reçus par la vue

        if(carteGriseR.getVehiculeId() == null){
            vehicule = new Vehicule(carteGriseR);

            if(carteGriseR.getEnergieId()==null)
                vehicule.setEnergie(null);
            else
                vehicule.setEnergie(energieService.findEnergie(carteGriseR.getEnergieId()));
            vehicule.setScore(100);
            vehicule.setOrganisation(organisation);
            if(carteGriseR.getMarqueVehiculeId()==null)
                vehicule.setMarqueVehicule(null);
            else
                vehicule.setMarqueVehicule(ms.findById(carteGriseR.getMarqueVehiculeId()));
        }
        else
            vehicule = vehiculeService.findById(carteGriseR.getVehiculeId());

        // retrouve l'objet visite en bd
        if(carteGriseR.getVisiteId()==null)
            throw new Exception("Prière de choisir une visite");
        Visite visite = visiteService.findById(carteGriseR.getVisiteId());
        vehicule.setOrganisation(visite.getOrganisation());
        //récupère l'id de la cg
        carteGrise.setCarteGriseId(visite.getCarteGrise().getCarteGriseId());
        carteGrise.setOrganisation(visite.getOrganisation());
        carteGrise.setProprietaireVehicule(pvs.findById(carteGriseR.getProprietaireId()));
        carteGrise.setProduit(ps.findById(carteGriseR.getProduitId()));
        carteGrise.setVehicule(vehicule);
        carteGrise.setOrganisation(organisation);

        visite.setCarteGrise(carteGrise);
        if (visite.getStatut()<1)
            visite.setStatut(1);

        if(!visiteService.enCoursVisitList(orgId).stream()
            .filter(visite1 -> visite1.getIdVisite() != carteGriseR.getVisiteId())
            .filter(visite1 -> visite1.getCarteGrise().getVehicule()!=null)
            .filter(visite1 -> visite1.getCarteGrise().getVehicule()
                    .getChassis().equals(carteGriseR.getChassis()))
            .collect(Collectors.toList()).isEmpty())
            throw new Exception("Ce chassis est déjà utilisé pour un véhicule en cours d'inspection");
        visite = visiteService.modifierVisite(visite);
        //carteGrise = cgs.updateCarteGrise(carteGrise);

        return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", visite.getCarteGrise() );
        }
		catch(Exception e){
			return ApiResponseHandler.generateResponse(HttpStatus.OK, false, e.getMessage(), null );
		}
    }
    @GetMapping("/api/v1/cartegrise/listview")
    public ResponseEntity<Object> carteGriseListView() {
        LOGGER.trace("Recherche carte grise...");
        try {
            LOGGER.trace("Liste des produits");
            Map<String, Object> listView;
            List<Map<String, Object>> mapList = new ArrayList<>();
            for (CarteGrise c : cgs.findAll()) {
                listView = new HashMap<>();
                listView.put("id", c.getCarteGriseId());
                listView.put("immatriculation", c.getNumImmatriculation());
                listView.put("proprietaire", c.getProprietaireVehicule().getPartenaire().getNom());
                listView.put("montant", c.getMontantPaye());
                listView.put("createdDate", c.getCreatedDate());
                listView.put("modifiedDate", c.getModifiedDate());
                mapList.add(listView);
            }

            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", mapList);

        } catch (Exception e) {
            return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, Message.ERREUR_LIST_VIEW + "Client", null);
        }

    }
    /*Admin*/
    @GetMapping("/api/v1/admin/cartegrises")
    public ResponseEntity<Object> findAllforAdmin() {
        LOGGER.trace("Recherche carte grise...");
        try {
            List<CarteGrise> cs = cgs.findAll();
            //cgs.findByImmatriculationOuCarteGrise(imCha)
            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", cs);
        } catch (Exception e) {
            return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, "Une erreur est survenue", null);

        }
    }

    @PostMapping("/api/v1/admin/cartegrises")
    public ResponseEntity<Object> saveCGforAdmin(@RequestBody CarteGrisePOJO c) {
        LOGGER.trace("Add CG...");


            CarteGrise carteGrise = new CarteGrise();
        carteGrise.setCarteGriseId(c.getCarteGriseId());
        carteGrise.setNumImmatriculation(c.getNumImmatriculation());
        carteGrise.setPreImmatriculation(c.getPreImmatriculation());
        carteGrise.setDateDebutValid(c.getDateDebutValid());
        carteGrise.setDateFinValid(c.getDateFinValid());
        carteGrise.setSsdt_id(c.getSsdt_id());
        carteGrise.setCommune(c.getCommune());
        carteGrise.setMontantPaye(c.getMontantPaye());
        carteGrise.setVehiculeGage(c.isVehiculeGage());
        carteGrise.setGenreVehicule(c.getGenreVehicule());

        carteGrise.setEnregistrement(c.getEnregistrement());
        carteGrise.setDateDelivrance(c.getDateDelivrance());
        carteGrise.setLieuDedelivrance(c.getLieuDedelivrance());
        carteGrise.setCentre_ssdt(c.getCentre_ssdt());
            carteGrise.setCarteGriseId(c.getCarteGriseId());

            carteGrise.setProprietaireVehicule(c.getProprietaire() == null ?
                    null : pvs.findById(c.getProprietaire().getId()));

            carteGrise.setProduit(c.getProduit() == null ?
                    null : ps.findById(c.getProduit().getId()) );

            carteGrise.setVehicule(c.getVehicule() == null ?
                    null : vs.findById(c.getVehicule().getId()) );

            System.out.println(ToStringBuilder.reflectionToString(c.getProprietaire()));

            carteGrise.setOrganisation(c.getOrganisationId() == null ?
                    null : os.findByOrganisationId(c.getOrganisationId().getId()));

            System.out.println(ToStringBuilder.reflectionToString(carteGrise));

            carteGrise = cgs.save(carteGrise);

            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", carteGrise);
        /*try { } catch (Exception e) {
            return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, "Une erreur est survenue", null);

        }*/
    }
    @DeleteMapping("/api/v1/admin/cartegrises/{id}")
    public ResponseEntity<Object> energie(@PathVariable Long id) {
        try {
            cgs.deleteById(id);
            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "succès"
                    , null);
        } catch (Exception e) {
            LOGGER.error("Une erreur est survenu lors de l'accès à la liste des adresses");
            return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, Message.ERREUR_ADD + "Energie", null);
        }
    }

    @GetMapping("/api/v1/admin/cartegrises/select")
    public ResponseEntity<Object> getLexiquesOfMtcforSelect(){

        List<CarteGrise> cats = cgs.findAll();
        List<Map<String, String>> catsSelect = new ArrayList<>();

        Map<String, String> cat;

        for(CarteGrise c: cats){
            cat = new HashMap<>();
            cat.put("id", String.valueOf(c.getCarteGriseId()));
            cat.put("name", c.getNumImmatriculation() +" | "
                    + (c.getOrganisation() == null? "Tous" : c.getOrganisation().getNom()));
            catsSelect.add(cat);
        }

        return ApiResponseHandler.generateResponse(HttpStatus.OK,
                true, "OK", catsSelect);
    }
}
