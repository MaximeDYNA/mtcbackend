package com.catis.controller;

import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import com.catis.model.entity.TaxeProduit;
import com.catis.objectTemporaire.ProduitTicketdto;
import com.catis.objectTemporaire.SearchVentedto;
import com.catis.objectTemporaire.TaxeTicketdto;
import com.catis.objectTemporaire.Ticketdto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.catis.controller.message.Message;
import com.catis.model.entity.DetailVente;
import com.catis.model.entity.Vente;
import com.catis.service.DetailVenteService;
import com.catis.service.OperationCaisseService;
import com.catis.service.VenteService;
import pl.allegro.finance.tradukisto.ValueConverters;

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

    @GetMapping( value = "/api/v1/ventes/listview")
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
    @PostMapping( value = "/api/v1/ventes/search")
    public ResponseEntity<Object> getTickets(@PathVariable SearchVentedto searchVentedto){
        List<Ticketdto> ticketdtos = venteService.findByRef(searchVentedto.getRef())
                .stream().map(vente -> new Ticketdto(vente.getNumFacture(),
                        vente.getClient() == null? vente.getContact().getPartenaire().getNom():vente.getClient().getPartenaire().getNom(),
                        vente.getClient() == null? vente.getContact().getPartenaire().getTelephone():vente.getClient().getPartenaire().getTelephone(),
                        Date.from(vente.getCreatedDate().atZone(ZoneId.systemDefault()).toInstant()),
                        vente.getDetailventes().stream().map(detailVente -> new ProduitTicketdto(detailVente.getReference(),
                                detailVente.getProduit().getLibelle(),detailVente.getPrix(),getPrix(detailVente.getPrix(),
                                detailVente.getProduit().getTaxeProduit()))).collect(Collectors.toList()),
                        vente.getMontantTotal(), convert(searchVentedto.getLang(), vente.getMontantTotal())))
                .collect(Collectors.toList());
        return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "Tickets", ticketdtos);
    }
    @GetMapping( value = "/api/v1/ventes/tickets/{lang}")
    public ResponseEntity<Object> getAllTickets(@PathVariable String lang){
        List<Ticketdto> ticketdtos = venteService.findAll()
                .stream().map(vente -> new Ticketdto(vente.getNumFacture(),
                        vente.getClient() == null ? vente.getContact().getPartenaire().getNom():vente.getClient().getPartenaire().getNom(),
                        vente.getClient() == null ? vente.getContact().getPartenaire().getTelephone():vente.getClient().getPartenaire().getTelephone(),
                        Date.from(vente.getCreatedDate().atZone(ZoneId.systemDefault()).toInstant()),
                        vente.getDetailventes().stream().map(detailVente -> new ProduitTicketdto(detailVente.getReference(),
                                detailVente.getProduit().getLibelle(),detailVente.getPrix(),getPrix(detailVente.getPrix(),
                                detailVente.getProduit().getTaxeProduit()))).collect(Collectors.toList()),
                        vente.getMontantTotal(), convert(lang, vente.getMontantTotal())))
                        .collect(Collectors.toList());
        return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "Tickets", ticketdtos);
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
    public double getPrix(double prixTTC, Set<TaxeProduit> taxeProduits){

        double sum = taxeProduits.stream()
                .map(taxeTicketdto -> new Double(taxeTicketdto.getTaxe().getValeur()))
                .reduce((a,b) -> a+b)
                .get();
        double prix = prixTTC *(100+ sum) /100;
        return prix;
    }
    public String convert(String lang, double price){
        ValueConverters converter;
        if (lang.equalsIgnoreCase("fr")) {
            converter = ValueConverters.FRENCH_INTEGER;
        } else
            converter = ValueConverters.ENGLISH_INTEGER;

        return converter.asWords((int) price);
    }

}
