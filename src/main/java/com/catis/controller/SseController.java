package com.catis.controller;

import com.catis.model.entity.Visite;
import com.catis.objectTemporaire.Listview;
import com.catis.service.CategorieTestVehiculeService;
import com.catis.service.GieglanFileService;
import com.catis.service.VisiteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@CrossOrigin
public class SseController {

    private static Logger log = LoggerFactory.getLogger(VisiteController.class);

    static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    static List<SseEmitter> emitters= new CopyOnWriteArrayList<>();

    /*@GetMapping(value="/public/subscribe",consumes = MediaType.ALL_VALUE)
    public SseEmitter subscribe(){
        System.out.println("---Subscribe---");
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        try{
            emitter.send(SseEmitter.event().name("INIT"));
        }catch(IOException e){
            log.error(e.getMessage());
        }
        emitters.add(emitter);
        emitter.onCompletion(()->emitters.remove(emitter));

        return emitter;
    }

    public static void  dispatchEdit(Visite visite, VisiteService vs,
                                     GieglanFileService gieglanFileService,
                                     CategorieTestVehiculeService catSer)  {

        for(SseEmitter emitter:emitters){
            try{
                log.info("-----sse----");
                if(visite.getStatut()==1){
                    emitter.send(SseEmitter.event().name("edit_visit").data(
                            buildListView(visite, vs, gieglanFileService,catSer)));
                    emitter.send(SseEmitter.event().name("controleur_visit").data(visite));
                }
                else{
                    Listview l = buildListView(visite, vs, gieglanFileService,catSer);
                    emitter.send(SseEmitter.event().name("edit_visit").data(l));
                }

            }catch(IOException e){
                log.info("---SSE ERROR---");
                emitters.remove(emitter);
            }
        }
    }

    //dispatching all event
    public static void dispatcheventoclients(Visite visite, VisiteService vs,
                                             GieglanFileService gieglanFileService,
                                             CategorieTestVehiculeService catSer ){
        for(SseEmitter emitter:emitters){
            try{
                emitter.send(SseEmitter.event().name("new_visit").data(
                        buildListView(visite, vs, gieglanFileService,catSer)));
            }catch(IOException e){
                emitters.remove(emitter);
            }
        }
    }*/

    public static Listview buildListView(Visite visite, VisiteService visiteService,
                                         GieglanFileService gieglanFileService,
                                         CategorieTestVehiculeService catSer ){
        Listview v = new Listview(visite, visiteService, gieglanFileService,catSer);
        v.setCategorie(visite.getCarteGrise().getProduit());

        if (visite.getCarteGrise().getProprietaireVehicule()
                .getPartenaire()
                .getNom()
                == null)
            v.setClient(null);
        else
            v.setClient(visite.getCarteGrise().getProprietaireVehicule()
                    .getPartenaire()
                    .getNom());
        v.setDate(visite.getCreatedDate().format(dateTimeFormatter));
        v.setCreatedAt(visite.getCreatedDate());
        v.setReference(visite.getCarteGrise().getNumImmatriculation());
        v.setStatut(visite.statutRender(visite.getStatut()));
        v.setType(visite.typeRender());
        return v;
    }

}
