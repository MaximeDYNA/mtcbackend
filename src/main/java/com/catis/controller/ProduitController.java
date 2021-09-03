package com.catis.controller;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.catis.objectTemporaire.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.catis.controller.exception.VisiteEnCoursException;
import com.catis.model.entity.CarteGrise;
import com.catis.model.entity.Posales;
import com.catis.model.entity.Produit;
import com.catis.model.entity.Taxe;
import com.catis.model.entity.TaxeProduit;
import com.catis.repository.FilesStorageService;
import com.catis.service.CarteGriseService;
import com.catis.service.CategorieProduitService;
import com.catis.service.PosaleService;
import com.catis.service.ProduitService;
import com.catis.service.TaxeProduitService;
import com.catis.service.TaxeService;
import com.catis.service.VisiteService;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin
public class ProduitController {

    @Autowired
    private ProduitService produitService;
    @Autowired
    private CarteGriseService cgs;
    @Autowired
    private VisiteService visiteService;
    @Autowired
    private PosaleService posaleService;
    @Autowired
    private CategorieProduitService categorieProduitService;
    @Autowired
    private TaxeProduitService tps;
    @Autowired
    private TaxeService taxeService;
    @Autowired
    HttpServletRequest request;
    @Autowired
    FilesStorageService storageService;

    private static Logger LOGGER = LoggerFactory.getLogger(ProduitController.class);

    @GetMapping("/api/v1/produits")
    public ResponseEntity<Object> listeDesProduits() {
        LOGGER.trace("liste des catégories...");
        return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", produitService.findProduitWithoutContreVisite());
    }

    @PostMapping("/api/v1/admin/produits")
    public ResponseEntity<Object> addProduit(@RequestBody ProduitPOJO produitPOJO) throws Exception {

        LOGGER.trace("liste des produits...");
        Produit produit = new Produit();
        produit.setLibelle(produitPOJO.getLibelle());
        produit.setDescription(produitPOJO.getDescription());
        produit.setPrix(produitPOJO.getPrix());
        produit.setDelaiValidite(produitPOJO.getDelaiValidite());
        produit.setCategorieProduit(produitPOJO.getCategorieProduitId() == null ? null : categorieProduitService.findById(produitPOJO.getCategorieProduitId()));
        produit.setImg(produitPOJO.getImg() ==  null ? null : produitService.saveImage(produitPOJO.getImg(),produitPOJO.getLibelle() ));

        produit = produitService.addProduit(produit);
        return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", produit );
		      
		   /* try {} 
		    catch (Exception e) {
		    	LOGGER.error("Erreur lors de l'ajout d'un produit");
				return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, "Erreur lors de l'ajout d'un produit", null);
		    }*/

    }
    @DeleteMapping("/api/v1/produits/{id}")
    public ResponseEntity<Object> addProduit(@PathVariable Long id) throws Exception {

            produitService.deleteById(id);
            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", null );

    }

    @RequestMapping("/api/v1/produits/hold")
    public ResponseEntity<Object> listeDesProduitsParOnglet(@RequestBody HoldData holdeleter) {
        LOGGER.trace("produits par onglet...");
        List<Produit> produits = new ArrayList<>();
        for (Posales posales : posaleService.findByNumberSessionCaisse(holdeleter.getNumber(), holdeleter.getSessionCaisseId())) {
            produits.add(posales.getProduit());
        }
        return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", produits);
    }


    @RequestMapping(value = "/api/v1/caisse/produits/reference/{imCha}")
    public ResponseEntity<Object> listeDesProduitsParReference(@PathVariable String imCha) throws VisiteEnCoursException {
        try {
                if(imCha.equalsIgnoreCase(null))
                imCha="";

                if (visiteService.visiteEncours(imCha,
                        Long.valueOf(UserInfoIn.getUserInfo(request).getOrganisanionId())))
                throw new VisiteEnCoursException("Une visite est déjà en cours");
                LOGGER.trace("liste des catégories...");
                List<Produit> produits = new ArrayList<>();
                for (CarteGrise cg : cgs.findByImmatriculationOuCarteGrise(imCha)) {
                    System.out.println("produit de carte grise");
                    produits.add(cg.getProduit());
                }


                if (visiteService.isVisiteInitial(imCha,
                        Long.valueOf(UserInfoIn.getUserInfo(request).getOrganisanionId()))) {
                    if (!cgs.isCarteGriseExist(imCha)) {
                        List<ProduitEtTaxe> pets = new ArrayList<>();
                        List<Taxe> taxes;
                        ProduitEtTaxe pet;
                        for (Produit produit : produitService.findProduitWithoutContreVisite()) {
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
                    } else {
                        List<ProduitEtTaxe> pets = new ArrayList<>();
                        ProduitEtTaxe pet = new ProduitEtTaxe();
                        List<Taxe> taxes = new ArrayList<>();
                        pet.setProduit(produitService.findByImmatriculation(imCha));

                        pet.setTaxe(taxeService.taxListByLibelle(produitService.findByLibelle("cv").getLibelle()));
                        pets.add(pet);

                        return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", pets);
                    }

                } else {

                    List<ProduitEtTaxe> pets = new ArrayList<>();
                    ProduitEtTaxe pet = new ProduitEtTaxe(produitService.findByLibelle("cv"), taxeService.taxListByLibelle("cv"));
                    pets.add(pet);


                    return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", pets);
                }
			
        }
			catch (VisiteEnCoursException vece) {
				LOGGER.error("visite déjà en cours! Veuillez vérifier la liste des visites en cours");
				return ApiResponseHandler.generateResponse(HttpStatus.OK, false, "Une visite est actuellement en cours pour ce véhicule", null);
			}catch (Exception e) {
				LOGGER.error("Veuillez signaler cette erreur à Franck");
				return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, "Veuillez signaler cette erreur à l'equipe CATIS", null);
			}

    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/v1/produits/listview")
    public ResponseEntity<Object> listViewProduits() {
        LOGGER.trace("Liste des produits");
        Map<String, Object> produitsListView;
        List<Map<String, Object>> mapList = new ArrayList<>();

        ListViewCatProduit pv;

        for (Produit p : produitService.findAllProduit()) {
            produitsListView = new HashMap<>();
            pv = new ListViewCatProduit();
            pv.setCreatedDate(p.getCreatedDate());
            pv.setModifiedDate(p.getModifiedDate());
            produitsListView.put("id", p.getProduitId());
            produitsListView.put("libelle", p.getLibelle());
            produitsListView.put("description", p.getDescription());
            produitsListView.put("prix", p.getPrix());
            produitsListView.put("CategorieProduit", p.getCategorieProduit().getLibelle());
            produitsListView.put("createdDate", pv.getCreatedDate());
            produitsListView.put("modifiedDate", pv.getModifiedDate());
            mapList.add(produitsListView);
        }

        return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", mapList);
    }

    /*Administration*/
    @GetMapping(value = "/api/v1/admin/produits")
    public ResponseEntity<Object> ProduitList() {

        List<Produit> produits = produitService.findAllProduit();

        return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", produits);
    }
    @GetMapping(value = "/api/v1/admin/produits/select")
    public ResponseEntity<Object> produitListForSelect() {
        try {
            List<Produit> results = produitService.findAllProduit();
            List<Map<String, String>> mapList = new ArrayList<>();
            Map<String, String> map = new HashMap<>();
            for(Produit produit : results){
                map.put("id",String.valueOf(produit.getProduitId()));
                map.put("name", produit.getLibelle());
                mapList.add(map);
                map = new HashMap<>();
            }
            return ApiResponseHandler.generateResponse(HttpStatus.OK, false, "OK", mapList);
        }
        catch(Exception e){
            e.printStackTrace();
            return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, "OK", null);
        }
    }
    @GetMapping("/api/v1/admin/produits/graphview/legende")
    public ResponseEntity<Object> legendeforGraphView() {
        try {
            //log
            List<String> legends = produitService.getLibelleList();
            String[] datas = new String[legends.size()];
            for (int i = 0; i < datas.length; i++) {
                datas[i] = legends.get(i);
            }

            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "Affichage graph view visit", datas);
        } catch (Exception e) {

            return ApiResponseHandler.generateResponse(HttpStatus.OK, false, "Erreur lors de l'affichage"
                    + " de la liste des visite en cours", null);
        }

    }
    @GetMapping("/api/v1/admin/produits/graphview/data")
    public ResponseEntity<Object> dataforGraphView() {
        try {
            //occurence produit dans la liste des visites
            Map<String, Integer> legendAndOccurence = produitService.getLibelleAndOccurence();

            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "Occurences de produit", legendAndOccurence);
        } catch (Exception e) {

            return ApiResponseHandler.generateResponse(HttpStatus.OK, false, "Erreur lors de l'affichage"
                    + " de la liste des visite en cours", null);
        }

    }
    /*@PostMapping("/api/v1/admin/produits")
    public ResponseEntity<Object> addProduitAdmin(
            @RequestParam("libelle") String libelle,
            @RequestParam("description") String description,
            @RequestParam("prix") double prix,
            @RequestParam("delaiValidite") int delaiValidite,
            @RequestParam("file") MultipartFile file,
            @RequestParam("categorieProduitId") Long categorieProduitId) throws Exception {


        Produit produit = new Produit();
        produit.setLibelle(libelle);
        produit.setDescription(description);
        produit.setPrix(prix);
        produit.setDelaiValidite(delaiValidite);
        produit.setCategorieProduit(categorieProduitService.findById(categorieProduitId));
        //produit.setImg(produitService.saveImage(file));
        LOGGER.trace("liste des catégories...");
        return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", produitService.addProduit(produit));

		   /* try {}
		    catch (Exception e) {
		    	LOGGER.error("Erreur lors de l'ajout d'un produit");
				return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, "Erreur lors de l'ajout d'un produit", null);
		    }


    }*/
    /*@GetMapping("/api/v1/admin/produits/graphview")
    public ResponseEntity<Object> listforGraphView() {
        try {

            List<Object> graphViews = new ArrayList<>();
            int[] datas = new int[produitService.findAllProduit().size()];
            for (int i = 0; i < datas.length; i++) {
                datas[i] = produitService.findAllProduit().size();
            }
            Map<String, int[]> result = new HashMap<>();
            result.put("tab", datas);
            graphViews.add(new GraphView("maj", vs.listParStatus(0).size()));
            graphViews.add(new GraphView("A inspecter", vs.listParStatus(1).size()));
            graphViews.add(new GraphView("En cours test", vs.listParStatus(2).size()));
            graphViews.add(new GraphView("A signer", vs.listParStatus(3).size()));
            graphViews.add(new GraphView("A imprimer", vs.listParStatus(4).size()));
            graphViews.add(new GraphView("A enregister", vs.listParStatus(5).size()));
            graphViews.add(new GraphView("A certifier", vs.listParStatus(6).size()));
            graphViews.add(new GraphView("Accepté", vs.listParStatus(7).size()));
            graphViews.add(new GraphView("Refusé", vs.listParStatus(8).size()));

            return ApiResponseHandler.generateResponses(HttpStatus.OK, true, "Affichage graph view visit", graphViews, datas);
        } catch (Exception e) {
            log.error("Erreur lors de l'affichage de la liste des visite en cours");
            return ApiResponseHandler.generateResponse(HttpStatus.OK, false, "Erreur lors de l'affichage"
                    + " de la liste des visite en cours", null);
        }

    }*/

}
