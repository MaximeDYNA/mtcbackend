package com.catis.controller;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import com.catis.controller.configuration.SessionData;
import com.catis.model.entity.*;
import com.catis.objectTemporaire.CarteGrisePOJO;
import com.catis.repository.MessageRepository;
import com.catis.service.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import com.catis.objectTemporaire.CarteGriseReceived;
import com.catis.objectTemporaire.UpdateCartegrise;

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
    private MessageRepository msgRepo;
    @Autowired
    HttpServletRequest request;

    @Autowired
    private PagedResourcesAssembler<CarteGrise> pagedResourcesAssembler;



    private static Logger LOGGER = LoggerFactory.getLogger(CarteGriseController.class);


    
    @CacheEvict(value = "VisiteCache", allEntries = true)
    @PutMapping("/api/v1/cartegrise/updateProprietaire")
    public ResponseEntity<Void> updateProprietaire(
            @RequestBody UpdateCartegrise updateDTO) {
        cgs.updateProprietaireVehicule(updateDTO.getCarteGriseId(), updateDTO.getNewProprietaireVehiculeId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/api/v1/cartegrise/search/{imCha}")
    public ResponseEntity<Object> search(@PathVariable String imCha) {
        LOGGER.trace("Recherche carte grise...");
        try {
            List<CarteGrise> carteGrises = cgs.findBychassis(imCha);
            //cgs.findByImmatriculationOuCarteGrise(imCha)
            LOGGER.info("Liste des cartes grises");
            Message msg = msgRepo.findByCode("CG007");
            return ApiResponseHandler.generateResponseWithAlertLevel(HttpStatus.OK, true, msg, carteGrises);
        } catch (Exception e) {
            LOGGER.error("Erreur liste des cartes grises");
            Message msg = msgRepo.findByCode("CG008");
            return ApiResponseHandler.generateResponseWithAlertLevel(HttpStatus.OK, false, msg, null);

        }
    }

    @GetMapping("/public/search/cartegrise/assurance/{imma}")
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
    @GetMapping("/public/search/cartegrise/assurance/imma/{imma}")
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
    @GetMapping("/public/cartegrise/assurance/visite/{imma}")
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
    @Transactional
    @CacheEvict(value = "VisiteCache", allEntries = true)
    public ResponseEntity<Object> misajour(@RequestBody CarteGriseReceived carteGriseR) throws IOException {
        LOGGER.trace("mise à jour demandé...");
        UUID orgId = SessionData.getOrganisationId(request);
        Organisation organisation = os.findByOrganisationId(orgId);
        LOGGER.info("ORGANISATION FETCHED");
        try {

        CarteGrise carteGrise = new CarteGrise(carteGriseR);
        Vehicule vehicule;

        LOGGER.info("CARTE GRISE AND VEHICULE INSTANCES CREATED");
        //initialise le vehicule avec les éléments reçus par la vue

        if(carteGriseR.getVehiculeId() == null){
            LOGGER.info("CARTE GRISE HAS NO VEHICULE ASSOCATION SO WE CREATE ONE");
            vehicule = new Vehicule(carteGriseR);
            if(carteGriseR.getEnergieId()==null)
            vehicule.setEnergie(null);
            else
            vehicule.setEnergie(energieService.findEnergie(carteGriseR.getEnergieId()));
            vehicule.setScore(100);
            if(carteGriseR.getMarqueVehiculeId()==null)
            vehicule.setMarqueVehicule(null);
            else
            vehicule.setMarqueVehicule(ms.findById(carteGriseR.getMarqueVehiculeId()));
            LOGGER.info("CREATED NEW VEHICLE FOR CARTE GRISE");
            }
            else{
            LOGGER.info("carte GRISE has vehicule, reassociation");
            vehicule = vehiculeService.findById(carteGriseR.getVehiculeId());
            vehicule.setTypeVehicule(carteGriseR.getTypeVehicule());
            vehicule.setCarrosserie(carteGriseR.getCarrosserie());
            vehicule.setPlaceAssise(carteGriseR.getPlaces());
            vehicule.setPremiereMiseEnCirculation(carteGriseR.getPremiereMiseEnCirculation());
            vehicule.setPuissAdmin(carteGriseR.getPuissAdmin());
            vehicule.setPoidsTotalCha(carteGriseR.getPoidsTotalCha());
            vehicule.setPoidsVide(carteGriseR.getPoidsVide());
            vehicule.setChargeUtile(carteGriseR.getChargeUtile());
            vehicule.setCylindre(vehicule.getCylindre()); // cm3
            if(carteGriseR.getMarqueVehiculeId()==null)
                vehicule.setMarqueVehicule(null);
            else
                vehicule.setMarqueVehicule(ms.findById(carteGriseR.getMarqueVehiculeId()));
            if(carteGriseR.getEnergieId()==null)
                vehicule.setEnergie(null);
            else
                vehicule.setEnergie(energieService.findEnergie(carteGriseR.getEnergieId()));
        }
        // retrouve l'objet visite en bd
        if(carteGriseR.getVisiteId()==null)
        throw new Exception("Prière de choisir une visite");
        LOGGER.info("searching visite based on carteGrise received");
        Visite visite = visiteService.findById(carteGriseR.getVisiteId());
        vehicule.setOrganisation(visite.getOrganisation());
        //récupère l'id de la cg
        carteGrise.setCarteGriseId(visite.getCarteGrise().getCarteGriseId());
        carteGrise.setOrganisation(visite.getOrganisation());
        carteGrise.setProprietaireVehicule(pvs.findById(carteGriseR.getProprietaireId()));
        carteGrise.setProduit(ps.findById(carteGriseR.getProduitId()));
        carteGrise.setVehicule(vehicule);
        carteGrise.setOrganisation(organisation);
        LOGGER.info("associating cartgrise to visite");
        visite.setCarteGrise(carteGrise);
        LOGGER.info("associated cartgrise to visite");
        if (visite.getStatut()<1)
            visite.setStatut(1);

        if(!visiteService.enCoursVisitList(orgId).stream()
            .filter(visite1 -> visite1.getIdVisite() != carteGriseR.getVisiteId()
                    && visite1.getCarteGrise().getVehicule()!=null
                    && visite1.getCarteGrise().getVehicule().getChassis().equals(carteGriseR.getChassis()))
            .collect(Collectors.toList()).isEmpty())
            throw new Exception("Ce chassis est déjà utilisé pour un véhicule en cours d'inspection");
            visite = visiteService.modifierVisite(visite);
            Message msg = msgRepo.findByCode("CG001");
            LOGGER.info("Enregistrement "+ carteGriseR.getNumImmatriculation()+" carte grise réussi");
            return ApiResponseHandler.generateResponseWithAlertLevel(HttpStatus.OK, true, msg, null );
            // return ApiResponseHandler.generateResponseWithAlertLevel(HttpStatus.OK, true, msg, visite.getCarteGrise() );
        }
		catch(Exception e){
            e.printStackTrace();
            Message msg = msgRepo.findByCode("CG002");
            LOGGER.info("Enregistrement "+ carteGriseR.getNumImmatriculation()+" carte grise réussi");
			return ApiResponseHandler.generateResponseWithAlertLevel(HttpStatus.OK, false, msg, null );
		}
    }
    @Transactional
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
            Message msg = msgRepo.findByCode("CG003");
            LOGGER.info("Affichage de la liste des cartes grises réussi");
            return ApiResponseHandler.generateResponseWithAlertLevel(HttpStatus.OK, true, msg, mapList);

        } catch (Exception e) {
            Message msg = msgRepo.findByCode("CG004");
            LOGGER.info("Erreur lors de l'affichage de la liste des cartes grises");
            return ApiResponseHandler.generateResponseWithAlertLevel(HttpStatus.OK, false, msg, null);
        }

    }
    /*Admin*/
    // @GetMapping("/api/v1/admin/cartegrises")
    // public ResponseEntity<Object> findAllforAdmin() {
    //     LOGGER.trace("Recherche carte grise...");
    //     try {
    //         List<CarteGrise> cs = cgs.findAll();
    //         Message msg = msgRepo.findByCode("CG003");
    //         LOGGER.info("Affichage de la liste des cartes grises réussi");
    //         return ApiResponseHandler.generateResponseWithAlertLevel(HttpStatus.OK, true, msg, cs);
    //     } catch (Exception e) {
    //         List<CarteGrise> cs = cgs.findAll();
    //         Message msg = msgRepo.findByCode("CG004");
    //         LOGGER.info("Erreur lors de l'affichage de la liste des cartes grises");
    //         return ApiResponseHandler.generateResponseWithAlertLevel(HttpStatus.OK, false, msg, null);

    //     }
    // }
    // flemming implimnted
    @GetMapping(value="/api/v1/admin/cartegrises",params = {"search","page", "size"})
    public ResponseEntity<Object> findAllforAdmin(@RequestParam("search") String search, @RequestParam("page") int page,
    @RequestParam("size") int size) {
        LOGGER.trace("Recherche carte grise...");
        if(search==null){
            search = "";
        }
        try {
            Page<CarteGrise> cs = cgs.findAllPage(search, PageRequest.of(page, size, Sort.by("createdDate").descending()));

            PagedModel<EntityModel<CarteGrise>> result = pagedResourcesAssembler
            .toModel(cs);
  
            Message msg = msgRepo.findByCode("CG003");
            LOGGER.info("Affichage de la liste des cartes grises réussi");
            return ApiResponseHandler.generateResponseWithAlertLevel(HttpStatus.OK, true, msg, result);
        } catch (Exception e) {
            e.printStackTrace();
            Message msg = msgRepo.findByCode("CG004");
            LOGGER.info("Erreur lors de l'affichage de la liste des cartes grises");
            return ApiResponseHandler.generateResponseWithAlertLevel(HttpStatus.OK, false, msg, null);
        }
    }

    @Transactional
    @PostMapping("/api/v1/admin/cartegrises")
    public ResponseEntity<Object> saveCGforAdmin(@RequestBody CarteGrisePOJO c) {
        LOGGER.trace("Add CG...");
        try {

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
            Message msg = msgRepo.findByCode("CG001");
            LOGGER.info("Enregistrement "+ c.getNumImmatriculation()+" carte grise réussi");

            return ApiResponseHandler.generateResponseWithAlertLevel(HttpStatus.OK, true, msg, carteGrise);
         } catch (Exception e) {
            e.printStackTrace();
            return ApiResponseHandler.generateResponse(HttpStatus.OK, false, "Une erreur est survenue", null);

        }
    }
    @Transactional
    @DeleteMapping("/api/v1/admin/cartegrises/{id}")
    public ResponseEntity<Object> energie(@PathVariable UUID id) {
        try {
            cgs.deleteById(id);
            Message msg = msgRepo.findByCode("CG006");
            LOGGER.info("Suppression carte grise réussi");

            return ApiResponseHandler.generateResponseWithAlertLevel(HttpStatus.OK, true, msg
                    , null);
        } catch (Exception e) {
            e.printStackTrace();
            Message msg = msgRepo.findByCode("CG005");
            LOGGER.error("Erreur lors de la suppression de la carte grise");
            return ApiResponseHandler.generateResponseWithAlertLevel(HttpStatus.OK, false, msg, null);
        }
    }

    @Transactional
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
