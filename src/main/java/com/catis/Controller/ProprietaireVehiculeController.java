package com.catis.Controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.catis.Controller.message.Message;
import com.catis.model.Client;
import com.catis.model.Partenaire;
import com.catis.model.ProprietaireVehicule;
import com.catis.objectTemporaire.ClientPartenaire;
import com.catis.service.OrganisationService;
import com.catis.service.PartenaireService;
import com.catis.service.ProprietaireVehiculeService;

@RestController
@CrossOrigin
public class ProprietaireVehiculeController {

	@Autowired
	private ProprietaireVehiculeService proprietaireVehiculeadresseService;
	@Autowired
	private PartenaireService partenaireService;
	@Autowired
	private OrganisationService os;
	
	private static Logger LOGGER = LoggerFactory.getLogger(ProprietaireVehiculeController.class);
	
	@GetMapping("/api/v1/proprietaires")
	public ResponseEntity<Object> proprioList() {
		 try {
			 LOGGER.info("List des propriétaires des vehicules...");
			 return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "succès"
					 , proprietaireVehiculeadresseService.findAll());
		 }
			
			catch(Exception e) {
				LOGGER.error("Une erreur est survenu lors de l'accès à la liste des adresses");
				return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, "Une erreur est survenu", null);
			}
	 }
	@PostMapping("/api/v1/proprietaires")
	public ResponseEntity<Object> addProprio(@RequestBody ClientPartenaire clientPartenaire) {
		 try {
			 LOGGER.info("Ajout d'un propriétaire...");
				
				ProprietaireVehicule pv = new ProprietaireVehicule();
				Partenaire partenaire = new Partenaire();
				partenaire.setCni(clientPartenaire.getCni());
				//System.out.println("*******************"+clientPartenaire.getNom());
				if(clientPartenaire.getDateNaiss()!=null) {
					Date date = new SimpleDateFormat("yyyy-MM-dd").parse(clientPartenaire.getDateNaiss()); ;
					partenaire.setDateNaiss(date);
				}
				else
					partenaire.setDateNaiss(null);	
				
				
				partenaire.setEmail(clientPartenaire.getEmail());
				partenaire.setTelephone(clientPartenaire.getTelephone());
				partenaire.setNom(clientPartenaire.getNom());
				partenaire.setPrenom(clientPartenaire.getPrenom());
				partenaire.setPassport(clientPartenaire.getPassport());
				partenaire.setLieuDeNaiss(clientPartenaire.getLieuDeNaiss());
				partenaire.setPermiDeConduire(clientPartenaire.getPermiDeConduire());
				partenaire.setOrganisation(os.findByOrganisationId(0L));
				pv.setPartenaire(partenaireService.addPartenaire(partenaire));
				pv.setDescription(clientPartenaire.getVariants());
				
				LOGGER.info("Ajout de "+ partenaire.getNom() +" réussi");
			 return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "succès"
					 , proprietaireVehiculeadresseService.addProprietaire(pv));
		 }
			
			catch(Exception e) {
				LOGGER.error("Une erreur est survenu l'ajout d'un proprietaire");
				return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, Message.ERREUR_ADD+"Propietaire", null);
			}
	 }
}
