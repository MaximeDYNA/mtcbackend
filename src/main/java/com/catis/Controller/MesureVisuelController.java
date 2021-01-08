package com.catis.Controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.catis.model.DefectsModel;
import com.catis.model.MesureVisuel;
import com.catis.model.defectresponse;
import com.catis.model.lexique_inspection;
import com.catis.service.MesureVisuelService;

@RestController
@CrossOrigin
public class MesureVisuelController {
	
	private List<lexique_inspection> defects;
	
	@Autowired
	private MesureVisuelService mesurevisuelservice;
	private static Logger LOGGER = LoggerFactory.getLogger(MesureVisuelController.class);
	
	@PostMapping("/api/v1/mesurevisuel")
	public  ResponseEntity<Object> addMesureVisuel(@RequestBody defectresponse defectrespons){
		
	
		
		defects = new ArrayList<>();
		
		  for(DefectsModel defectsmodel : defectrespons.getDefectslist()){
		  
			  defects.add(new lexique_inspection(defectsmodel.getId(),defectrespons.getInspectionid()));
		  }
		  mesurevisuelservice.addMesureVisuel(defects);
		  
		  LOGGER.info("List des mesures visuelles...List<MesureVisuel> mesurevisuel "
		  +defectrespons.getDefectslist()); 
			/*
			 * for(DefectsModel h : defectrespons.getDefectslist()) {
			 * LOGGER.info("List des mesures"+h.getDefect()); }
			 */
		 
		try {
				return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", null);
			} 
		catch(Exception e){ 
				return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, "Une erreur est survenue", null );
				  
		}
	}
	
	
	@PostMapping("/api/v1/datainspection")
	public  ResponseEntity<Object> addDataInspection(@RequestBody MesureVisuel mesurevisuel){
		
		System.out.println("hello "+mesurevisuel.toString());
		  LOGGER.info("List des mesures visuelles...List<MesureVisuel> mesurevisuel "
		  +mesurevisuel.getImage1()); 
			/*
			 * for(DefectsModel h : defectrespons.getDefectslist()) {
			 * LOGGER.info("List des mesures"+h.getDefect()); }
			 */
		 
		try {
				return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", mesurevisuelservice.addDataInspection(mesurevisuel));
			} 
		catch(Exception e){ 
				return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, "Une erreur est survenue", null );
				  
		}
	}
	
	
}
