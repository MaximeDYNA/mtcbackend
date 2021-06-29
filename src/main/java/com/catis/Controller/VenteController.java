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
import org.springframework.web.bind.annotation.*;

import com.catis.Controller.message.Message;
import com.catis.model.DetailVente;
import com.catis.model.Vente;
import com.catis.service.DetailVenteService;
import com.catis.service.OperationCaisseService;
import com.catis.service.VenteService;

@RestController
@CrossOrigin
public class VenteController {
    @Autowired
    private VenteService venteService;
    @Autowired
    private DetailVenteService detailVenteService;
    @Autowired
    private OperationCaisseService ocs;
    private static Logger LOGGER = LoggerFactory.getLogger(VenteController.class);

    @RequestMapping(method = RequestMethod.GET, value = "/api/v1/ventes/listview")
    public ResponseEntity<Object> listVentes() {

        LOGGER.trace("Liste vente");
        Map<String, Object> venteListView;
        List<Map<String, Object>> mapList = new ArrayList<>();
        for (Vente v : venteService.findAll()) {
            venteListView = new HashMap<>();
            venteListView.put("id", v.getIdVente());
            venteListView.put("client", v.getClient().getPartenaire().getNom() + " " + v.getClient().getPartenaire().getPrenom());
            venteListView.put("vendeur", v.getVendeur().getPartenaire().getNom() + " " + v.getVendeur().getPartenaire().getPrenom());
            venteListView.put("montantTotal", v.getMontantTotal());
            venteListView.put("contact", v.getContact().getPartenaire().getNom() + " " + v.getContact().getPartenaire().getPrenom());
            venteListView.put("montantHT", v.getMontantHT());
            venteListView.put("facture", v.getNumFacture());
            venteListView.put("montantEncaisse", ocs.montantTotalEncaisse(v.getIdVente()));
            //venteListView.put("statut", v.getLibelleStatut());
            venteListView.put("createdDate", v.getCreatedDate());

            mapList.add(venteListView);
        }
        return ApiResponseHandler.generateResponse(HttpStatus.OK, true, Message.OK_LIST_VIEW + "Vente", mapList);
			/*try {}
			
		} catch (Exception e) {
			LOGGER.error(Message.ERREUR_LIST_VIEW +"Vente");
			return ApiResponseHandler.generateResponse(HttpStatus.OK, true , Message.ERREUR_LIST_VIEW +"Vente", null );
		}*/
    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/v1/ventes/{id}/detailsvente/listview")
    public ResponseEntity<Object> listVentes(@PathVariable Long id) {
        try {
            LOGGER.trace("Liste détails vente");
            Map<String, Object> venteListView;
            List<Map<String, Object>> mapList = new ArrayList<>();
            for (DetailVente v : detailVenteService.findByVente(id)) {
                venteListView = new HashMap<>();
                venteListView.put("detailId", v.getIdDetailVente());
                venteListView.put("produit", v.getProduit().getLibelle());
                venteListView.put("ref", v.getReference());
                venteListView.put("createdDate", v.getCreatedDate());
                venteListView.put("modifiedDate", v.getModifiedDate());
                //venteListView.put("createdDate", v.getCreatedDate());
                mapList.add(venteListView);
            }
            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, Message.OK_LIST_VIEW + "Détails Vente", mapList);
        } catch (Exception e) {
            LOGGER.error(Message.ERREUR_LIST_VIEW + "Vente");
            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, Message.ERREUR_LIST_VIEW + "Détails Vente", null);
        }
    }

    @PostMapping("/api/v1/ventes")
    public ResponseEntity<Object> addVente(@RequestBody Vente vente) {
        try {

            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "Succès", venteService.addVente(vente));
        } catch (Exception e) {
            LOGGER.error(Message.ERREUR_LIST_VIEW + "Vente");
            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, Message.ERREUR_LIST_VIEW + "Vente", null);
        }
    }
    @GetMapping("/api/v1/admin/ventes")
    public ResponseEntity<Object> getVentes() {


            List<Vente> ventes = venteService.findAll();

            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "Succès", ventes);
           /* try { } catch (Exception e) {
            LOGGER.error(Message.ERREUR_LIST_VIEW + "Vente");
            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, Message.ERREUR_LIST_VIEW + "Vente", null);
        }*/
    }
    @GetMapping("/api/v1/admin/ventes/ca/today")
    public ResponseEntity<Object> getCAToday() {


        double ca = venteService.getTodayCA();

        return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "Succès", ca);
           /* try { } catch (Exception e) {
            LOGGER.error(Message.ERREUR_LIST_VIEW + "Vente");
            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, Message.ERREUR_LIST_VIEW + "Vente", null);
        }*/
    }


}
