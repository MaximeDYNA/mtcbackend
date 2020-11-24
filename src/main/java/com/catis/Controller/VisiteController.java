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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.catis.model.Client;
import com.catis.model.Visite;
import com.catis.objectTemporaire.GraphView;
import com.catis.objectTemporaire.KabanViewVisit;
import com.catis.objectTemporaire.Listview;
import com.catis.service.CarteGriseService;
import com.catis.service.ProduitService;
import com.catis.service.VariableView;
import com.catis.service.VenteService;
import com.catis.service.VisiteService;

@RestController
@CrossOrigin
public class VisiteController {

	@Autowired
	private VisiteService vs;
	@Autowired
	private ProduitService ps;
	@Autowired
	private VenteService venteService;
	@Autowired
	private CarteGriseService cgs;
	
	private static Logger log  = LoggerFactory.getLogger(VisiteController.class);
	
	private VariableView v;
	@RequestMapping(method=RequestMethod.GET, value="/api/v1/visitesencours")
	public ResponseEntity<Object> listDesVisitesEncours(){
		try {
			log.info("Liste des visites en cours");
			return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "liste des visite en cours", vs.enCoursVisitList());
		} catch (Exception e) {
			log.error("Erreur lors de l'affichage de la liste des visite en cours");
			return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "Erreur lors de l'affichage"
					+ " de la liste des visite en cours", null);
		}
		
	}
	@RequestMapping(method=RequestMethod.GET, value="/api/v1/visite/codestatut/{status}")
	public ResponseEntity<Object> visiteByStatut(@PathVariable int status){
		try {
			log.info("Liste des visites en cours");
			return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "liste des visite en cours", vs.listParStatus(status));
		} catch (Exception e) {
			log.error("Erreur lors de l'affichage de la liste des visite en cours");
			return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "Erreur lors de l'affichage"
					+ " de la liste des visite en cours", null);
		}
		
	}
	@RequestMapping(method=RequestMethod.GET, value="/api/v1/visites")
	public ResponseEntity<Object> visites(){
		try {
			log.info("Liste des visites");
			return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "liste des visite en cours", vs.findAll());
		} catch (Exception e) {
			log.error("Erreur lors de l'affichage de la liste des visite en cours");
			return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "Erreur lors de l'affichage"
					+ " de la liste des visite en cours", null);
		}
		
	}
	@RequestMapping(method=RequestMethod.GET, value="/api/v1/visit/kanbanview")
	public ResponseEntity<Object> listforKabanView(){
		
		
		try {
			
			
			log.info("kaban view visit");
			List <KabanViewVisit> kabanViewVisits = new ArrayList<>();
			kabanViewVisits.add(new KabanViewVisit("maj", vs.listParStatus(0), vs.listParStatus(0).size()));
			kabanViewVisits.add(new KabanViewVisit("A inspecter", vs.listParStatus(1), vs.listParStatus(1).size()));
			kabanViewVisits.add(new KabanViewVisit("En cours test", vs.listParStatus(2), vs.listParStatus(2).size()));
			kabanViewVisits.add(new KabanViewVisit("A signer", vs.listParStatus(3), vs.listParStatus(3).size()));
			kabanViewVisits.add(new KabanViewVisit("A imprimer", vs.listParStatus(4), vs.listParStatus(4).size()));
			kabanViewVisits.add(new KabanViewVisit("A enregister", vs.listParStatus(5), vs.listParStatus(5).size()));
			kabanViewVisits.add(new KabanViewVisit("A certifier", vs.listParStatus(6), vs.listParStatus(6).size()));
			kabanViewVisits.add(new KabanViewVisit("Accepté", vs.listParStatus(7), vs.listParStatus(7).size()));
			kabanViewVisits.add(new KabanViewVisit("Refusé", vs.listParStatus(8), vs.listParStatus(8).size()));
			return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "Affichage Kaban view visit", kabanViewVisits);
		} catch (Exception e) {
			log.error("Erreur lors de l'affichage de la liste des visite en cours");
			return ApiResponseHandler.generateResponse(HttpStatus.OK, false, "Erreur lors de l'affichage"
					+ " de la liste des visi"
					+ "te en cours", null);
		}
		
	}
	@RequestMapping(method=RequestMethod.GET, value="/api/v1/visite/graphview")
	public ResponseEntity<Object> listforGraphView(){
		try {
			log.info("Graphe view visit");
			List <Object> graphViews = new ArrayList<>();
			int[] datas = new int[9];
			for(int i=0;i<datas.length;i++) {
				datas[i]=vs.listParStatus(i).size();
			}
			Map<String, int[]> result = new HashMap<>();
			result.put("tab", datas);
			graphViews.add(new GraphView("maj",  vs.listParStatus(0).size()));
			graphViews.add(new GraphView("A inspecter",  vs.listParStatus(1).size()));
			graphViews.add(new GraphView("En cours test",  vs.listParStatus(2).size()));
			graphViews.add(new GraphView("A signer", vs.listParStatus(3).size()));
			graphViews.add(new GraphView("A imprimer", vs.listParStatus(4).size()));
			graphViews.add(new GraphView("A enregister",  vs.listParStatus(5).size()));
			graphViews.add(new GraphView("A certifier",  vs.listParStatus(6).size()));
			graphViews.add(new GraphView("Accepté",  vs.listParStatus(7).size()));
			graphViews.add(new GraphView("Refusé",vs.listParStatus(8).size()));
			
			return ApiResponseHandler.generateResponses(HttpStatus.OK, true, "Affichage graph view visit", graphViews,datas);
		} catch (Exception e) {
			log.error("Erreur lors de l'affichage de la liste des visite en cours");
			return ApiResponseHandler.generateResponse(HttpStatus.OK, false, "Erreur lors de l'affichage"
					+ " de la liste des visite en cours", null);
		}
		
	}
	@RequestMapping(method=RequestMethod.GET, value="/api/v1/visite/listview")
	public ResponseEntity<Object> listforlistView(){
		
			log.info("list view visit");
			List<Listview> listVisit = new ArrayList<>();
			for(Visite visite: vs.enCoursVisitList()) {
				Listview lv = new Listview();
				lv.setCategorie(ps.findByImmatriculation(visite.getCarteGrise()
						.getNumImmatriculation()));
				
				if (venteService.findByVisite(visite.getIdVisite())
						 == null)
					lv.setClient(null);
				else
				lv.setClient(venteService.findByVisite(visite.getIdVisite())
						.getClient()
						.getPartenaire()
						.getNom());
				lv.setDate(visite.getDateDebut());
				lv.setReference(visite.getCarteGrise().getNumImmatriculation());
				lv.setStatut(visite.statutRender(visite.getStatut()));
				lv.setType(visite.typeRender());
				listVisit.add(lv);
				lv.setId(visite.getIdVisite());
				
			}
			return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "Affichage en mode liste des visites", listVisit);
			
			
		/*try {} catch (Exception e) {
			log.error("Erreur lors de l'affichage de la liste des visite en cours");
			return ApiResponseHandler.generateResponse(HttpStatus.OK, false, "Erreur lors de l'affichage en mode liste des visites encours", null);
		}*/
		
	}
	
}
