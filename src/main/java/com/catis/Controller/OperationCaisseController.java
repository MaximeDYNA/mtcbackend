package com.catis.Controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.catis.model.entity.OperationCaisse;
import com.catis.objectTemporaire.OpCaisseDTO;
import com.catis.objectTemporaire.RecapDTO;
import com.catis.service.OperationCaisseService;
import com.catis.service.VenteService;

@RestController
public class  OperationCaisseController {


    private OperationCaisseService ocs;
    private VenteService venteService;

    @Autowired
    public OperationCaisseController(OperationCaisseService ocs, VenteService venteService) {
        super();
        this.ocs = ocs;
        this.venteService = venteService;
    }

    private static Logger LOGGER = LoggerFactory.getLogger(OperationCaisseController.class);

    @GetMapping("/api/v1/operationcaisse/{code}/listview")
    public ResponseEntity<Object> reglementListView(@PathVariable int code) {

            LOGGER.trace("Liste des adresses demandée");
            Map<String, Object> reglementListView;
            List<Map<String, Object>> mapList = new ArrayList<>();
            for (OperationCaisse o : ocs.encaissementList(code)) {
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
            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", mapList);
        /*try {} catch (Exception e) {
            LOGGER.error("Erreur");
            return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, Message.ERREUR_LIST_VIEW + "règlement", null);
        }*/
    }

    @PostMapping("/api/v1/operationcaisse/recap")
    public ResponseEntity<Object> recap(@RequestBody RecapDTO recapDTO) {

            LOGGER.trace("Recapitulatif demandé");

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime start = LocalDateTime.parse(recapDTO.getDateDebut(), formatter);
            LocalDateTime end = LocalDateTime.parse(recapDTO.getDateFin(), formatter);
            List<OpCaisseDTO> ops = venteService.recapOp(recapDTO.getCaissierId(), start, end);
            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", ops);
           /* try { } catch (Exception e) {
            LOGGER.error("Erreur");
            return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, Message.ERREUR_LIST_VIEW + "recap", null);
        }*/
    }

}
