package com.catis.controller;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.catis.objectTemporaire.CatProductForSelectDTO;
import com.catis.objectTemporaire.CategorieproduitProduitPOJO;
import com.catis.service.OrganisationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.catis.model.entity.CategorieProduit;
import com.catis.model.entity.Produit;
import com.catis.model.entity.Taxe;
import com.catis.model.entity.TaxeProduit;
import com.catis.objectTemporaire.ListViewCatProduit;
import com.catis.objectTemporaire.ProduitEtTaxe;
import com.catis.service.CategorieProduitService;
import com.catis.service.ProduitService;
import com.catis.service.TaxeProduitService;

@RestController
@CrossOrigin
@Validated
public class CategorieProduitController {

    /*
     * @Autowired private CategorieProduitService categorieProduitService;
     */
    @Autowired
    private ProduitService produitService;
    @Autowired
    private CategorieProduitService cateProduitService;
    @Autowired
    private TaxeProduitService tps;
    @Autowired
    private OrganisationService os;


    private Logger LOGGER = LoggerFactory.getLogger(CategorieProduitController.class);

    @RequestMapping("/api/v1/caisse/categorieproduits/{categorieId}/listesproduits")
    public ResponseEntity<Object> listerLesProduits(@PathVariable String categorieId) throws IllegalArgumentException {
        try {
            List<ProduitEtTaxe> pets = new ArrayList<>();
            List<Taxe> taxes;
            ProduitEtTaxe pet;
            Long id = Long.valueOf(categorieId);
            for (Produit produit : produitService.findByCategorieProduit(id).stream()
                    .filter(prod -> !prod.getLibelle().equalsIgnoreCase("cv"))
                    .collect(Collectors.toList())) {
                pet = new ProduitEtTaxe();
                pet.setProduit(produit);
                taxes = new ArrayList<>();
                for (TaxeProduit tp : tps.findByProduitId(produit.getProduitId())) {
                    taxes.add(tp.getTaxe());
                }
                pet.setTaxe(taxes);
                pets.add(pet);
            }

            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", pets);
        } catch (java.lang.IllegalArgumentException il) {
            LOGGER.error("Identifiant catégorie produit incorrect");
            return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, "Identifiant catégorie produit incorrect", null);
        }

    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/v1/caisse/catproducts")
    public ResponseEntity<Object> categorieProduits() {
        try {
            LOGGER.trace("Liste des catégories");
            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", cateProduitService.listeCategorieProduit());
        } catch (Exception e) {
            LOGGER.error("Erreur lors de l'ajout d'une catégorie.");
            return ApiResponseHandler.generateResponse(HttpStatus.OK, false, "false", null);
        }

    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/v1/catproducts/listview")
    public ResponseEntity<Object> catProduits() {


        LOGGER.trace("Liste des catégories");
        List<ListViewCatProduit> l = new ArrayList<>();
        for (CategorieProduit cp : cateProduitService.listeCategorieProduit()) {
            ListViewCatProduit lvct = new ListViewCatProduit();

            lvct.setLibelle(cp.getLibelle());
            lvct.setCreatedDate(cp.getCreatedDate());
            lvct.setModifiedDate(cp.getModifiedDate());
            lvct.setActiveStatus(cp.getActiveStatus());
            l.add(lvct);
        }
        return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", l);
		/*try{} catch (Exception e) {
			LOGGER.error("Erreur lors de l'affichage des catégories de produit."); 
			return ApiResponseHandler.generateResponse(HttpStatus.OK, false, "false",  null);
		}*/

    }


    //***Admin***//

    @GetMapping("/api/v1/admin/catproducts")
    public List<CategorieProduit> catproduct() {
        try {
            LOGGER.trace("Liste des catégories");
            return  cateProduitService.listeCategorieProduit();
        } catch (Exception e) {
            LOGGER.error("Erreur lors de l'ajout d'une catégorie.");
            return null;
        }

    }
    @GetMapping("/api/v1/admin/catproducts/forselect")
    public List<CatProductForSelectDTO> catProductForSelectDTOS() {
        try {
            LOGGER.trace("Liste des catégories");
            List<CatProductForSelectDTO> cat = new ArrayList<>();
            cateProduitService.listeCategorieProduit()
                    .forEach(categorieProduit -> {
                        cat.add(new CatProductForSelectDTO(categorieProduit.getCategorieProduitId(),
                                categorieProduit.getLibelle()));
                    });
            return cat;
        } catch (Exception e) {
            LOGGER.error("Erreur lors de l'ajout d'une catégorie.");
            return null;
        }

    }
    /***admin**/
    @RequestMapping(method = RequestMethod.POST, value = "/api/v1/admin/categorieproduits")
    public ResponseEntity<Object> addCategorieProduits(@RequestBody CategorieproduitProduitPOJO categorieProduit) {

            LOGGER.trace("Ajout d'une catégorie");
            CategorieProduit c = new CategorieProduit();
            c.setCategorieProduitId(categorieProduit.getCategorieProduitId());
            c.setLibelle(categorieProduit.getLibelle());
            c.setDescription(categorieProduit.getDescription());
            c.setOrganisation( categorieProduit.getOrganisation() == null? null :
                    os.findByOrganisationId(
                            categorieProduit
                                    .getOrganisation()
                                    ));

            c = cateProduitService.addCategorieProduit(c);
            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", c);
        /*try { } catch (Exception e) {
            LOGGER.error("Erreur lors de l'ajout d'une catégorie.");
            return ApiResponseHandler.generateResponse(HttpStatus.OK, false, "false", null);
        }*/

    }
    @RequestMapping(method = RequestMethod.DELETE, value = "/api/v1/categorieproduits/{id}")
    public ResponseEntity<Object> deleteCategorieProduits(@PathVariable Long id) {

        LOGGER.trace("suppression de la catégorie produit ID = "+ id);

        cateProduitService.deleteCategorieProduit(id);
        return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", null);
        /*try { } catch (Exception e) {
            LOGGER.error("Erreur lors de l'ajout d'une catégorie.");
            return ApiResponseHandler.generateResponse(HttpStatus.OK, false, "false", null);
        }*/

    }
    @GetMapping("/api/v1/categorieproduits/select")
    public ResponseEntity<Object> getCaissesOfMtcforSelect(){

        List<CategorieProduit> cats = cateProduitService.listeCategorieProduit();
        List<Map<String, String>> catsSelect = new ArrayList<>();

        Map<String, String> cat;

        for(CategorieProduit c: cats){
            cat = new HashMap<>();
            cat.put("id", String.valueOf(c.getCategorieProduitId()));
            cat.put("name", c.getLibelle() +" | "
                    + (c.getOrganisation() == null? "Tous" : c.getOrganisation().getNom()));
            catsSelect.add(cat);
        }

        return ApiResponseHandler.generateResponse(HttpStatus.OK,
                true, "Select catégorie produit OK", catsSelect);
    }
}