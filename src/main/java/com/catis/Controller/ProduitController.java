package com.catis.Controller;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.catis.Controller.exception.VisiteEnCoursException;
import com.catis.model.CarteGrise;
import com.catis.model.Posales;
import com.catis.model.Produit;
import com.catis.model.Taxe;
import com.catis.model.TaxeProduit;
import com.catis.objectTemporaire.HoldData;
import com.catis.objectTemporaire.ProduitEtTaxe;
import com.catis.repository.FilesStorageService;
import com.catis.service.CarteGriseService;
import com.catis.service.CategorieProduitService;
import com.catis.service.PosaleService;
import com.catis.service.ProduitService;
import com.catis.service.TaxeProduitService;
import com.catis.service.TaxeService;
import com.catis.service.VisiteService;

import pl.allegro.finance.tradukisto.MoneyConverters;

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
	FilesStorageService storageService;

	private static Logger LOGGER = LoggerFactory.getLogger(ProduitController.class);
	
	@GetMapping("/api/v1/produits")
	public ResponseEntity<Object> listeDesProduits() {
		LOGGER.info("liste des catégories...");
		return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", produitService.findProduitWithoutContreVisite());
	}
	
	@RequestMapping(method= RequestMethod.POST, value="/api/v1/produits")
	public ResponseEntity<Object> addProduit(
	@RequestParam("produitId")Long produitId,
	@RequestParam("libelle") String libelle,
	@RequestParam("description") String description,
	@RequestParam("prix") double prix,
	@RequestParam("delaiValidite") int delaiValidite,
	@RequestParam("file") MultipartFile file,
	@RequestParam("categorieProduitId") Long categorieProduitId) {
			
		    try {
		    	
		    	Produit produit = new Produit();
		    	produit.setLibelle(libelle);
		    	produit.setDescription(description);
		    	produit.setPrix(prix);
		    	produit.setDelaiValidite(delaiValidite);
		    	produit.setCategorieProduit(categorieProduitService.findById(categorieProduitId));
		    	produit.setImg(produitService.saveImage(file));
		      LOGGER.info("liste des catégories...");
		      return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", 	produitService.addProduit(produit));
		      
		    } 
		    catch (Exception e) {
		    	LOGGER.error("Erreur lors de l'ajout d'un produit");
				return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, "Erreur lors de l'ajout d'un produit", null);
		    }
		    
		    
		
		
		
	}
	/*@PostMapping("/upload")
	  public ResponseEntity<Object> uploadFile(@RequestParam("img") Produit produit) {
	    String message = "";
	    try {
	    	 try {
	    	      Files.copy(file.getInputStream(), this.root.resolve(file.getOriginalFilename()));
	    	    } catch (Exception e) {
	    	      throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
	    	    }
	      storageService.save(file);

	      message = "Uploaded the file successfully: " + file.getOriginalFilename();
	      return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
	    } catch (Exception e) {
	      message = "Could not upload the file: " + file.getOriginalFilename() + "!";
	      return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
	    }
	  }
	*/
	
	@RequestMapping("/api/v1/produits/hold")
	public ResponseEntity<Object> listeDesProduitsParOnglet(@RequestBody HoldData holdeleter ) {
		LOGGER.info("produits par onglet...");
		List <Produit> produits = new ArrayList<>();
		for(Posales posales : posaleService.findByNumberSessionCaisse(holdeleter.getNumber(), holdeleter.getSessionCaisseId())) {
			produits.add(posales.getProduit());
		}
		return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", produits);
	}
	
	
	
	@RequestMapping(value="/api/v1/produits/reference/{imCha}")
	public ResponseEntity<Object> listeDesProduitsParReference(@PathVariable String imCha) throws VisiteEnCoursException {
		try {
				if(visiteService.visiteEncours(imCha))
					throw new VisiteEnCoursException("Une visite est déjà en cours");
				LOGGER.info("liste des catégories...");
				List<Produit> produits = new ArrayList<>();
				for(CarteGrise cg : cgs.findByImmatriculationOuCarteGrise(imCha)) {
					System.out.println("produit de carte grise");
					produits.add(cg.getProduit());
				}
	
				
				if(!visiteService.viensPourContreVisite(imCha)) {
					
					if(cgs.findByImmatriculationOuCarteGrise(imCha).isEmpty()) {
						List<ProduitEtTaxe> pets = new ArrayList<>();
						List<Taxe> taxes;
						ProduitEtTaxe pet; 
						for(Produit produit : produitService.findProduitWithoutContreVisite()) {
							pet = new ProduitEtTaxe();
							pet.setProduit(produit);
							taxes = new ArrayList<>();
							for(TaxeProduit tp: tps.findByProduitId(produit.getProduitId())) {
								taxes.add(tp.getTaxe());
							}
							pet.setTaxe(taxes);
							pets.add(pet);
							
						}
						return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", pets);
					}
					else {
						List<ProduitEtTaxe> pets = new ArrayList<>();
						ProduitEtTaxe pet = new ProduitEtTaxe();
						List <Taxe> taxes = new ArrayList<>();
						pet.setProduit(produitService.findByImmatriculation(imCha));
						
						pet.setTaxe(taxeService.taxListByLibelle(produitService.findByLibelle("cv").getLibelle()));
						pets.add(pet);
						//Visite v =  visiteService.findByReference(imCha).get(0);
						return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", pets);
					}
						
				}
				
				else {
					
					List<ProduitEtTaxe> pets = new ArrayList<>();
					ProduitEtTaxe pet = new ProduitEtTaxe(produitService.findByLibelle("cv"), taxeService.taxListByLibelle("cv") );
					pets.add(pet);
					
					
					return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", pets );
				}
			
			} catch (Exception e) {
				LOGGER.error("Veuillez signaler cette erreur à Franck");
				return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, "Veuillez signaler cette erreur à l'equipe CATIS", null);
			}
		
	}

	@RequestMapping(method= RequestMethod.GET, value="/api/v1/produits/listview")
	public ResponseEntity<Object> listViewProduits() {
		LOGGER.info("Liste des produits");
		Map<String ,Object> produitsListView; 
		List<Map<String ,Object>> mapList = new ArrayList<>();
		for(Produit p : produitService.findAllProduit()) {
			produitsListView = new HashMap<>();
			produitsListView.put("id", p.getProduitId());
			produitsListView.put("libelle", p.getLibelle());
			produitsListView.put("description", p.getDescription());
			produitsListView.put("prix", p.getPrix());
			produitsListView.put("CategorieProduit", p.getCategorieProduit().getLibelle());
			produitsListView.put("createdDate", p.getCreatedDate());
			produitsListView.put("modifiedDate", p.getModifiedDate());
			mapList.add(produitsListView);
		}
		
		return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", mapList);
	}
	
}
