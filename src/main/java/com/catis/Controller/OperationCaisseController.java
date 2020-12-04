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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.catis.Controller.message.Message;
import com.catis.model.OperationCaisse;
import com.catis.service.OperationCaisseService;

@RestController
public class OperationCaisseController {

	@Autowired
	private OperationCaisseService ocs;
	
	private static Logger LOGGER = LoggerFactory.getLogger(OperationCaisseController.class);
	
	@GetMapping("/api/v1/reglement/listview")
	public ResponseEntity<Object> reglementListView() {
		try {
			LOGGER.info("Liste des adresses demandée");
			Map<String ,Object> reglementListView; 
			List<Map<String ,Object>> mapList = new ArrayList<>();
			for(OperationCaisse o : ocs.encaissementList(2)) {
				reglementListView = new HashMap<>();
				reglementListView.put("id", o.getOperationDeCaisseId());
				
				reglementListView.put("ticket", o.getNumeroTicket());
				reglementListView.put("montant", o.getMontant());
				//reglementListView.put("taxe", o.getTaxe().getValeur());
				reglementListView.put("nom", o.getVente().getContact().getPartenaire().getNom());
				reglementListView.put("createdDate", o.getCreatedDate());
				reglementListView.put("modifiedDate", o.getModifiedDate());
				mapList.add(reglementListView);
			}
			return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success",  mapList);
		} catch (Exception e) {
			LOGGER.error("Erreur");
			return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, Message.ERREUR_LIST_VIEW + "règlement",  null);
		}
		
			
			
	}
	
}
