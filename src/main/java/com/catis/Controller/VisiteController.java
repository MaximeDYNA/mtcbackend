package com.catis.Controller;

import java.io.IOException;
import java.time.Duration;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import com.catis.service.*;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;

import com.catis.model.Visite;
import com.catis.objectTemporaire.GraphView;
import com.catis.objectTemporaire.KabanViewVisit;
import com.catis.objectTemporaire.Listview;


import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.DirectProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxProcessor;
import reactor.core.publisher.FluxSink;

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
    @Autowired
    private GieglanFileService gieglanFileService;
    @Autowired
    private CategorieTestVehiculeService catSer;

    private static Logger log = LoggerFactory.getLogger(VisiteController.class);


    static List<SseEmitter> emitters= new CopyOnWriteArrayList<>();


    @CrossOrigin
    @GetMapping(value="/api/v1/subscribe",consumes = MediaType.ALL_VALUE)
    public SseEmitter  subscribe(){

        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        try{
            emitter.send(SseEmitter.event().name("INIT"));
        }catch(IOException e){
            e.printStackTrace();
        }
        emitters.add(emitter);
        emitter.onCompletion(()->emitters.remove(emitter));

        return emitter;
    }
    @GetMapping(value="/api/v1/dispatchedit",consumes = MediaType.ALL_VALUE)
    public static void  dispatchEdit(Visite visite, VisiteService vs,
                                    GieglanFileService gieglanFileService,
                                    CategorieTestVehiculeService catSer, ProduitService ps){
        for(SseEmitter emitter:emitters){
            try{

                emitter.send(SseEmitter.event().name("edit_visit").data(
                        buildListView(visite, vs, gieglanFileService,catSer, ps)));

            }catch(IOException e){
                emitters.remove(emitter);
            }
        }
    }

    //dispatching all event
    @PostMapping(value = "/api/v1/dispatchevent")
    public static void dispatcheventoclients(Visite visite, VisiteService vs,
                                             GieglanFileService gieglanFileService,
                                             CategorieTestVehiculeService catSer, ProduitService ps ){
        for(SseEmitter emitter:emitters){
            try{
                //System.out.println("emit " + emitters.size());
                emitter.send(SseEmitter.event().name("new_visit").data(
                        buildListView(visite, vs, gieglanFileService,catSer, ps)));
            }catch(IOException e){
                emitters.remove(emitter);
            }
        }
    }

    @GetMapping(value = "/api/v1/visitesencours")
    public ResponseEntity<Object> listDesVisitesEncours() {

        log.info("Liste des visites en cours");
        List<Listview> listVisit = new ArrayList<>();
        vs.enCoursVisitList().forEach( visite -> {
           listVisit.add(buildListView(visite, vs, gieglanFileService,catSer, ps));
        });
        return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "OK", listVisit);

    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/v1/visite/codestatut/{status}")
    public ResponseEntity<Object> visiteByStatut(@PathVariable int status) {
        try {
            log.info("Liste des visites en cours");

            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "liste des visite en cours", vs.listParStatus(status));
        } catch (Exception e) {
            log.error("Erreur lors de l'affichage de la liste des visite en cours");
            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "Erreur lors de l'affichage"
                    + " de la liste des visite en cours", null);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/v1/visites")
    public ResponseEntity<Object> visites() {
        try {
            log.info("Liste des visites");
            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "liste des visite en cours", vs.findAll());
        } catch (Exception e) {
            log.error("Erreur lors de l'affichage de la liste des visite en cours");
            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "Erreur lors de l'affichage"
                    + " de la liste des visite en cours", null);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/v1/visit/kanbanview")
    public ResponseEntity<Object> listforKabanView() {
        try {
                log.info("kaban view visit");
                List<KabanViewVisit> kabanViewVisits = new ArrayList<>();
                kabanViewVisits.add(new KabanViewVisit("maj", vs.listParStatus(0), vs.listParStatus(0).size()));
                kabanViewVisits.add(new KabanViewVisit("A inspecter", vs.listParStatus(1), vs.listParStatus(1).size()));
                kabanViewVisits.add(new KabanViewVisit("En cours test", vs.listParStatus(2), vs.listParStatus(2).size()));
                kabanViewVisits.add(new KabanViewVisit("A signer", vs.listParStatus(3), vs.listParStatus(3).size()));
                kabanViewVisits.add(new KabanViewVisit("A imprimer", vs.listParStatus(4), vs.listParStatus(4).size()));
                kabanViewVisits.add(new KabanViewVisit("A enregister", vs.listParStatus(5), vs.listParStatus(5).size()));
                kabanViewVisits.add(new KabanViewVisit("A certifier", vs.listParStatus(6), vs.listParStatus(6).size()));
                kabanViewVisits.add(new KabanViewVisit("Accepté", vs.listParStatus(7), vs.listParStatus(7).size()));
                kabanViewVisits.add(new KabanViewVisit("Refusé", vs.listParStatus(8), vs.listParStatus(8).size()));
                kabanViewVisits.add(new KabanViewVisit("A approuver", vs.listParStatus(9), vs.listParStatus(9).size()));
            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "Affichage Kaban view visit", kabanViewVisits);
        } catch (Exception e) {
            log.error("Erreur lors de l'affichage de la liste des visite en cours");
            return ApiResponseHandler.generateResponse(HttpStatus.OK, false, "Erreur lors de l'affichage"
                    + " de la liste des visi"
                    + "te en cours", null);
        }

    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/v1/visite/graphview")
    public ResponseEntity<Object> listforGraphView() {
        try {
            log.info("Graphe view visit");
            List<Object> graphViews = new ArrayList<>();
            int[] datas = new int[9];
            for (int i = 0; i < datas.length; i++) {
                datas[i] = vs.listParStatus(i).size();
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

    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/v1/visite/listview")
    public ResponseEntity<Object> listforlistView() {

        log.info("list view visit");
        List<Listview> listVisit = new ArrayList<>();
        for (Visite visite : vs.listParStatus(0)) {
            Listview lv = new Listview(visite.getIdVisite(), vs,gieglanFileService,catSer);
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

    @RequestMapping(method = RequestMethod.GET, value = "/api/v1/visite/listview/{statutCode}")
    public ResponseEntity<Object> listforlistView(@PathVariable int statutCode) {

        log.info("list view visit");
        List<Listview> listVisit = new ArrayList<>();
        vs.listParStatus(statutCode).forEach(
                visite -> {
                    listVisit.add(buildListView(visite, vs, gieglanFileService,catSer, ps));
                }
        );
        return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "Affichage en mode liste des visites", listVisit);
		
		/*try {} catch (Exception e) {
			log.error("Erreur lors de l'affichage de la liste des visite en cours");
			return ApiResponseHandler.generateResponse(HttpStatus.OK, false, "Erreur lors de l'affichage en mode liste des visites encours", null);
		}*/
    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/v1/visites/approuver/{visiteId}")
    public ResponseEntity<Object> approuver(@PathVariable Long visiteId) {
        try {
            log.info("Demande d'approbation d'une visite...");
            Visite visite = vs.findById(visiteId);
            visite = vs.approuver(visite);
            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "Visite approuvée avec succès", visite);
        } catch (Exception e) {
            log.error("Erreur lors de l'approbation");
            return ApiResponseHandler.generateResponse(HttpStatus.OK, false, "Erreur lors de l'approbation", null);
        }
    }
    public static Listview buildListView(Visite visite, VisiteService vs,
                                 GieglanFileService gieglanFileService,
                                 CategorieTestVehiculeService catSer, ProduitService ps ){
        Listview lv = new Listview(visite.getIdVisite(), vs, gieglanFileService,catSer);
        lv.setCategorie(ps.findByImmatriculation(visite.getCarteGrise()
                .getNumImmatriculation()));

        if (visite.getCarteGrise().getProprietaireVehicule()
                .getPartenaire()
                .getNom()
                == null)
            lv.setClient(null);
        else
            lv.setClient(visite.getCarteGrise().getProprietaireVehicule()
                    .getPartenaire()
                    .getNom());
        lv.setDate(visite.getDateDebut());
        lv.setReference(visite.getCarteGrise().getNumImmatriculation());
        lv.setStatut(visite.statutRender(visite.getStatut()));
        lv.setType(visite.typeRender());
        return lv;
    }

}
