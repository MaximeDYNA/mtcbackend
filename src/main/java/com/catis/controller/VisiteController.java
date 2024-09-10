package com.catis.controller;

import java.awt.*;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.AttributedString;
import java.time.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import com.catis.controller.configuration.SessionData;
import com.catis.controller.exception.ImpressionException;
import com.catis.controller.pdfhandler.MediaReplacedElementFactory;
import com.catis.controller.pdfhandler.PdfGenaratorUtil;
import com.catis.model.entity.*;
import com.catis.objectTemporaire.*;
import com.catis.repository.*;
import com.catis.service.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lowagie.text.DocumentException;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.*;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;


import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;


@RestController
@CrossOrigin
public class VisiteController {
    @Autowired
    private Environment environment;
    @Autowired
    HttpServletRequest request;
    @Autowired
    private VisiteRepository visiteRepo;

    @Autowired
    private OpenAlprService openAlprService;
    @Autowired
    MessageRepository messageRepository;

    @Autowired FindRapportListService rapportListService;

    @Autowired
    PdfGenaratorUtil pdfGenaratorUtil;
    @Autowired
    private VisiteService visiteService;

    @Autowired
    private InspectionService inspectionService;

    @Autowired
    private RapportDeVisiteRepo rapportDeVisiteRepo;

    @Autowired
    private MesureVisuelRepository mesureVisuelRepository;

    @Autowired
    private VenteService venteService;
    @Autowired
    private TaxeService taxeService;

    @Autowired
    FilesStorageService storageService;

    @Autowired
    private ProduitService ps;


    @Autowired
    private GieglanFileService gieglanFileService;
    @Autowired
    private CategorieTestVehiculeService catSer;

    @Autowired
    private PagedResourcesAssembler<NewListView> pagedResourcesAssembler;

    @Autowired
    private PagedResourcesAssembler<Visite> pagedVisiteResourcesAssembler;

    @Autowired
    private PagedResourcesAssembler<Visite> pagedResourcesAssemblerVisite;
    @Autowired
    private MessageRepository msgRepo;
    @Autowired
    private NotificationService notificationService;

    private static Logger log = LoggerFactory.getLogger(VisiteController.class);

    static List<SseEmitter> emitters= new CopyOnWriteArrayList<>();




    // @GetMapping(value="/api/v1/all/visites/search")
    // public ResponseEntity<List<VisiteSearchService>> findVisiteByChassisOrByCarteGriseImmatriculationOrByPartnerName(@RequestParam String searchTerm) {
    //     List<VisiteSearchService> results = searchservice.findVisiteBYChassisORByCarteGriseImmatriculationOrByPartnerNom(searchTerm);
    //     return new ResponseEntity<>(results, HttpStatus.OK);
    // }


    // @GetMapping(value = "/api/v1/all/visites", params = { "title", "page", "size" })
    public ResponseEntity<Object> listDesVisitesEncours(@RequestParam("title") String search, @RequestParam("page") int page,
                                                        @RequestParam("size") int size) {
        log.info("recherche ---");
        try{
            UUID orgId = SessionData.getOrganisationId(request);
            if(search == "" ){
                search=null;
            }
            List<Visite> resultPage = visiteService.searchedVisitList(search, orgId, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate")) );

            List<NewListView> newListViews = resultPage.stream().map(visite ->
            // visite.getCarteGrise().getProduit(), 

            new NewListView(visite.getIdVisite(), visite.getCarteGrise().getProduit(), visite.typeRender(), visite.getCarteGrise().getNumImmatriculation(),
                    (visite.getCarteGrise().getVehicule()==null
                            ? "": (visite.getCarteGrise().getVehicule().getChassis()==null
                            ? "" : visite.getCarteGrise().getVehicule().getChassis())),
                    (visite.getCarteGrise().getProprietaireVehicule()
                            .getPartenaire()
                            .getNom()
                             == null
                            ? null : visite.getCarteGrise().getProprietaireVehicule()
                            .getPartenaire()
                            .getNom()),
                    Utils.parseDate(visite.getCreatedDate()), visite.getCreatedDate(),
                    getHTML(visite), visite.getStatut(), visite.getIdVisite(),visite.isContreVisite(),
                    visite.getInspection() == null
                            ? null : visite.getInspection().getIdInspection(), visite.getCarteGrise(), visite.getOrganisation().isConformity(),
                    visite.getIsConform(),
                    visite.getOrganisation().getNom() ,visite.getInspection() == null? "" : visite.getInspection().getBestPlate(),
                    visite.getInspection() == null? 0 :visite.getInspection().getDistancePercentage(),
                    visite.getCreatedDate().format(SseController.dateTimeFormatter), false, visite.getDocument())
        ).collect(Collectors.toList());

            Page<NewListView> pages = new PageImpl<>(newListViews, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate")),300);
            PagedModel<EntityModel<NewListView>> result = pagedResourcesAssembler
                    .toModel(pages);
            log.info("Affichage de la liste des visites");
            Message msg = msgRepo.findByCode("VS001");
            return ApiResponseHandler.generateResponseWithAlertLevel(HttpStatus.OK, true, msg, result);
        }catch (Exception e){
            e.printStackTrace();
            Message msg = msgRepo.findByCode("VS002");
            return ApiResponseHandler.generateResponseWithAlertLevel(HttpStatus.OK, false, msg, null);
        }

    }

    // @GetMapping(value = "/api/v1/all/visites", params = { "title", "page", "size" })
    public ResponseEntity<Object> MainlistDesVisitesEncours(@RequestParam("title") String search,
                                                        @RequestParam("page") int page,
                                                        @RequestParam("size") int size) {
        log.info("recherche ---");
        try {
            UUID orgId = SessionData.getOrganisationId(request);
            Page<NewListView> pages = visiteService.NewsearchedVisitList(search, orgId, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate")));
            PagedModel<EntityModel<NewListView>> result = pagedResourcesAssembler.toModel(pages);
            log.info("Affichage de la liste des visites");
            Message msg = msgRepo.findByCode("VS001");
            return ApiResponseHandler.generateResponseWithAlertLevel(HttpStatus.OK, true, msg, result);
        } catch (Exception e) {
            e.printStackTrace();
            Message msg = msgRepo.findByCode("VS002");
            return ApiResponseHandler.generateResponseWithAlertLevel(HttpStatus.OK, false, msg, null);
        }
    }


    // Flemming implimented
    @GetMapping(value = "/api/v1/all/visites", params = { "title", "page", "size" })
    public ResponseEntity<Object> llistDesVisitesEncours(@RequestParam("title") String search, @RequestParam("page") int page, @RequestParam("size") int size) {
        try {
            if(search == "" ){
                search="";
            }
            UUID orgId = SessionData.getOrganisationId(request);

            Page<NewListView> resultPage = visiteService.searchedVisitMainList(search, orgId, PageRequest.of(page, size, Sort.Direction.DESC, "createdDate"));
            PagedModel<EntityModel<NewListView>> result = pagedResourcesAssembler.toModel(resultPage);

            log.info("Affichage de la liste des visites a encours");
            Message msg = msgRepo.findByCode("VS003");
            return ApiResponseHandler.generateResponseWithAlertLevel(HttpStatus.OK, true, msg, result);
        } catch (Exception e) {
            e.printStackTrace();
            Message msg = msgRepo.findByCode("VS004");
            return ApiResponseHandler.generateResponseWithAlertLevel(HttpStatus.OK, false, msg, null);
        }
    }

    // flemming implimented
    @GetMapping(value="/api/v1/all/visite/listview/{statutCode}", params = {"title","page", "size" })
    public ResponseEntity<Object> listforMainlistView(@PathVariable int statutCode, @RequestParam("title") String search, @RequestParam("page") int page, @RequestParam("size") int size) {
        try {
            if(search == "" ){
                search="";
            }
            UUID orgId = SessionData.getOrganisationId(request);

            Page<NewListView> resultPage = visiteService.searchedVisitMainListstatus(search, orgId, statutCode, PageRequest.of(page, size, Sort.Direction.DESC, "createdDate"));
            PagedModel<EntityModel<NewListView>> result = pagedResourcesAssembler.toModel(resultPage);

            log.info("Affichage de la liste des visites a inspecte");
            Message msg = msgRepo.findByCode("VS003");
            return ApiResponseHandler.generateResponseWithAlertLevel(HttpStatus.OK, true, msg, result);
        } catch (Exception e) {
            e.printStackTrace();
            Message msg = msgRepo.findByCode("VS004");
            return ApiResponseHandler.generateResponseWithAlertLevel(HttpStatus.OK, false, msg, null);
        }
    }

   
    // @GetMapping(value="/api/v1/all/visite/listview/{statutCode}", params = { "title", "page", "size" })
    public ResponseEntity<Object> listforlistView(@PathVariable int statutCode, @RequestParam("title") String search, @RequestParam("page") int page,
    @RequestParam("size") int size) {
        try{
            log.info("list view visit");
            UUID orgId = SessionData.getOrganisationId(request);
            if(search == "" ){
                search=null;
            }
            List<Visite> resultPage = visiteService.searchedVisitListstatus(search, orgId, statutCode, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate")) );

            List<NewListView> newListViews = resultPage.stream().map(visite ->
            // visite.getCarteGrise().getProduit(), 

                    new NewListView(visite.getIdVisite(),visite.getCarteGrise().getProduit(),visite.typeRender(), visite.getCarteGrise().getNumImmatriculation(),
                            (visite.getCarteGrise().getVehicule()==null
                                    ? "": (visite.getCarteGrise().getVehicule().getChassis()==null
                                    ? "" : visite.getCarteGrise().getVehicule().getChassis())),
                            (visite.getCarteGrise().getProprietaireVehicule()
                                    .getPartenaire()
                                    .getNom()
                                    == null
                                    ? null : visite.getCarteGrise().getProprietaireVehicule()
                                    .getPartenaire()
                                    .getNom()),
                            Utils.parseDate(visite.getCreatedDate()), visite.getCreatedDate(),
                            getHTML(visite), visite.getStatut(), visite.getIdVisite(),visite.isContreVisite(),
                            visite.getInspection() == null
                                    ? null : visite.getInspection().getIdInspection(), visite.getCarteGrise(), visite.getOrganisation().isConformity(),
                            visite.getIsConform(),
                            visite.getOrganisation().getNom() ,visite.getInspection() == null? "" : visite.getInspection().getBestPlate(),
                            visite.getInspection() == null? 0 :visite.getInspection().getDistancePercentage(),
                            visite.getCreatedDate().format(SseController.dateTimeFormatter), false, visite.getDocument())
            ).collect(Collectors.toList());

            Page<NewListView> pages = new PageImpl<>(newListViews, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate")),300);
            PagedModel<EntityModel<NewListView>> result = pagedResourcesAssembler
                    .toModel(pages);
            log.info("Affichage de la liste des visites");
            Message msg = msgRepo.findByCode("VS001");
            return ApiResponseHandler.generateResponseWithAlertLevel(HttpStatus.OK, true, msg, result);
        }catch (Exception e){
            e.printStackTrace();
            log.error("erreur de l'affichage de la liste des visites");
            Message msg = msgRepo.findByCode("VS002");
            return ApiResponseHandler.generateResponseWithAlertLevel(HttpStatus.OK, false, msg, null);
        }

    }


    @GetMapping(value="/api/v1/all/visite/list/{orgId}", params = { "title", "page", "size" })
    public ResponseEntity<Object> listlistView(
            @PathVariable("orgId") UUID orgId,
            @RequestParam("title") String search,
            @RequestParam("page") int page,
            @RequestParam("size") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdDate");
        List<Visite> visiteList = visiteService.getsearchedVisitList(search, orgId, pageable);

        return ResponseEntity.ok(visiteList);
    }
  



    // flemming implimented
    @GetMapping(value = "/api/v1/all/visitesended", params = { "page", "size" })
    public ResponseEntity<Object> getAllActiveVisites(@RequestParam("page") int page,
                                                      @RequestParam("size") int size) {
        try {
            UUID orgId = SessionData.getOrganisationId(request);

            Page<NewListView> resultPage = visiteService.endedMainVisitList(orgId, PageRequest.of(page, size, Sort.Direction.DESC, "createdDate"));
            PagedModel<EntityModel<NewListView>> result = pagedResourcesAssembler.toModel(resultPage);

            log.info("Affichage de la liste des visites");
            Message msg = msgRepo.findByCode("VS003");
            return ApiResponseHandler.generateResponseWithAlertLevel(HttpStatus.OK, true, msg, result);
        } catch (Exception e) {
            e.printStackTrace();
            Message msg = msgRepo.findByCode("VS004");
            return ApiResponseHandler.generateResponseWithAlertLevel(HttpStatus.OK, false, msg, null);
        }
    }


    // @GetMapping(value = "/api/v1/all/visitesended", params = {"page", "size"})
    // public ResponseEntity<Object> getAllAcitveVisetMain(@RequestParam("page") int page,
    //                                                 @RequestParam("size") int size) {
    //     try {
    //         UUID orgId = SessionData.getOrganisationId(request);

    //         Page<NewListView> resultPage = visiteService.getAllActiveVisites(orgId, PageRequest.of(page, size));
    //         PagedModel<EntityModel<NewListView>> result = pagedResourcesAssembler.toModel(resultPage);

    //         Message msg = msgRepo.findByCode("VS003");
    //         return ApiResponseHandler.generateResponseWithAlertLevel(HttpStatus.OK, true, msg, result);
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //         Message msg = msgRepo.findByCode("VS004");
    //         return ApiResponseHandler.generateResponseWithAlertLevel(HttpStatus.OK, false, msg, null);
    //     }
    // }



    // oldversion
    // @GetMapping(value = "/api/v1/all/visitesended", params = { "page", "size" })
    public ResponseEntity<Object> getAllAcitveViset(@RequestParam("page") int page,
                                                    @RequestParam("size") int size) {
        try{
        UUID orgId = SessionData.getOrganisationId(request);


            Page<Visite> resultPage = visiteService.endedVisitList(orgId, PageRequest.of(page, size));
            log.info("fetch success preparing to return visites visites");
        List<NewListView> newListViews = resultPage.stream().map(visite ->
                new NewListView(visite.getIdVisite(),visite.getCarteGrise().getProduit(), visite.typeRender(), visite.getCarteGrise().getNumImmatriculation(),
                        (visite.getCarteGrise().getVehicule()==null
                                ? "": (visite.getCarteGrise().getVehicule().getChassis()==null
                                ? "" : visite.getCarteGrise().getVehicule().getChassis())),
                        (visite.getCarteGrise().getProprietaireVehicule()
                                .getPartenaire()
                                .getNom()
                                == null
                                ? null : visite.getCarteGrise().getProprietaireVehicule()
                                .getPartenaire()
                                .getNom()),
                        Utils.parseDate(visite.getCreatedDate()), visite.getCreatedDate(),
                        getHTML(visite), visite.getStatut(), visite.getIdVisite(),visite.isContreVisite(),
                        visite.getInspection()==null? null : visite.getInspection().getIdInspection(), visite.getCarteGrise(), visite.getOrganisation().isConformity(),
                        visite.getIsConform(),
                        visite.getOrganisation().getNom() ,visite.getInspection()==null? null : visite.getInspection().getBestPlate(), visite.getInspection()==null? 0 : visite.getInspection().getDistancePercentage(),
                        visite.getCreatedDate().format(SseController.dateTimeFormatter), false, visite.getDocument())
        ).collect(Collectors.toList());

        /*log.info("Liste des visites terminées");
        resultPage.forEach(visite ->
            listVisit.add(buildListView(visite, visiteService, gieglanFileService,catSer))
        );*/

            
            Page<NewListView> pages = new PageImpl<>(newListViews, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id")), visiteService.endedVisitList(orgId).size());
            PagedModel<EntityModel<NewListView>> result = pagedResourcesAssembler
                    .toModel(pages);
            log.info("Affichage de la liste des visites");
            Message msg = msgRepo.findByCode("VS003");
            return ApiResponseHandler.generateResponseWithAlertLevel(HttpStatus.OK, true, msg, result);
        }catch (Exception e){
            e.printStackTrace();
            Message msg = msgRepo.findByCode("VS004");
            return ApiResponseHandler.generateResponseWithAlertLevel(HttpStatus.OK, false, msg, null);

        }


    }

    

    // flemming implimented
    @GetMapping(value="/api/v1/visite/codestatut/{status}", params = { "page", "size" })
    public ResponseEntity<Object> MainvisiteByStatut(@PathVariable int status,
    @RequestParam("page") int page,
    @RequestParam("size") int size) {
        try {
            UUID orgId = SessionData.getOrganisationId(request);
            Page<NewListView> resultPage = visiteService.MainlistParStatus(status, orgId, PageRequest.of(page, size, Sort.Direction.DESC, "createdDate"));
            PagedModel<EntityModel<NewListView>> result = pagedResourcesAssembler.toModel(resultPage);

            log.info("Affichage de la liste des visites a inspecte");
            Message msg = msgRepo.findByCode("VS003");
            return ApiResponseHandler.generateResponseWithAlertLevel(HttpStatus.OK, true, msg, result);
        } catch (Exception e) {
            e.printStackTrace();
            Message msg = msgRepo.findByCode("VS004");
            return ApiResponseHandler.generateResponseWithAlertLevel(HttpStatus.OK, false, msg, null);
        }
    }

    // old version suffers from n+1 query problem
    // @GetMapping(value="/api/v1/visite/codestatut/{status}", params = { "page", "size" })
    public ResponseEntity<Object> visiteByStatut(@PathVariable int status,
                                                 @RequestParam("page") int page,
                                                 @RequestParam("size") int size) {
        try {
            log.info("Liste des visites en cours");
            UUID orgId = SessionData.getOrganisationId(request);
            Page<Visite> resultPage = visiteService.listParStatus(status,orgId, PageRequest.of(page, size, Sort.Direction.DESC, "createdDate"));
            log.info("preparing data to be returned");
    
            PagedModel<EntityModel<Visite>> result = pagedResourcesAssemblerVisite
                    .toModel(resultPage);
            Message msg = msgRepo.findByCode("VS005");
            return ApiResponseHandler.generateResponseWithAlertLevel(HttpStatus.OK, true, msg, result);
        } catch (Exception e) {
            e.printStackTrace();
            Message msg = msgRepo.findByCode("VS006");
            return ApiResponseHandler.generateResponseWithAlertLevel(HttpStatus.OK, false, msg, null);
        }
    }

    // @GetMapping("/api/v1/visites")
    // public ResponseEntity<Object> visites() {
        //     try {
            //         log.info("Liste des visites");
            //         Page<Visite> resultPage = visiteService.findAll();
            //         PagedModel<EntityModel<Visite>> result = pagedVisiteResourcesAssembler.toModel(resultPage);
            //         return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "liste des visite en cours", result);
            //     } catch (Exception e) {
                //         e.printStackTrace();
                //         log.error("Erreur lors de l'affichage de la liste des visite en cours");
                //         return ApiResponseHandler.generateResponse(HttpStatus.OK, false, "Erreur lors de l'affichage"
                //                 + " de la liste des visite en cours", null);
                //     }
                // }
                // old method
    @GetMapping("/api/v1/visites")
    public ResponseEntity<Object> visitesOLd() {
        try {
            log.info("Liste des visites");
            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "liste des visite en cours", visiteService.findAll());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Erreur lors de l'affichage de la liste des visite en cours");
            return ApiResponseHandler.generateResponse(HttpStatus.OK, false, "Erreur lors de l'affichage"
                    + " de la liste des visite en cours", null);
        }
    }

    

    // flemming implimented 
    @GetMapping("/api/v1/visit/kanbanview")
    public ResponseEntity<Map<String, Object>> getVisiteStatusCountByOrganisation() {
        UUID organisationId = SessionData.getOrganisationId(request);
        Map<String, Object> statusCountMap = visiteService.MainlistParStatusForkanban(organisationId);
        return ResponseEntity.ok(statusCountMap);
   }

//    old version
    // @GetMapping("/api/v1/visit/kanbanview")
    public ResponseEntity<Object> listforKabanView() {
              long startTime = System.currentTimeMillis();
                try{
                UUID orgId = SessionData.getOrganisationId(request);
                List<KabanViewVisit> kabanViewVisits = new ArrayList<>();
                List<KanBanSimpleData> majs = visiteService.listParStatusForkanban(0, orgId);
                List<KanBanSimpleData> inspects = visiteService.listParStatusForkanban(1, orgId);
                List<KanBanSimpleData> tests = visiteService.listParStatusForkanban(2, orgId);
                List<KanBanSimpleData> toSign = visiteService.listParStatusForkanban(3, orgId);
                List<KanBanSimpleData> confirms = visiteService.listParStatusForkanban(4, orgId);
                List<KanBanSimpleData> unconforms = visiteService.listParStatusForkanban(5, orgId);
                List<KanBanSimpleData> prints = visiteService.listParStatusForkanban(6, orgId);
                List<KanBanSimpleData> refused = visiteService.listParStatusForkanban(7, orgId);
                List<KanBanSimpleData> certifies = visiteService.listParStatusForkanban(8, orgId);
                List<KanBanSimpleData> accepted = visiteService.listParStatusForkanban(9, orgId);
                List<KanBanSimpleData> approuve = visiteService.listParStatusForkanban(10, orgId);
                kabanViewVisits.add(new KabanViewVisit("maj", majs , majs.size()));
                kabanViewVisits.add(new KabanViewVisit("A inspecter", inspects, inspects.size()));
                kabanViewVisits.add(new KabanViewVisit("En cours test", tests, tests.size()));
                kabanViewVisits.add(new KabanViewVisit("A signer", toSign, toSign.size()));
                kabanViewVisits.add(new KabanViewVisit("En Attente conformité",  confirms, confirms.size()));
                kabanViewVisits.add(new KabanViewVisit("Non conforme", unconforms, unconforms.size()));
                kabanViewVisits.add(new KabanViewVisit("A imprimer", prints, prints.size()));
                kabanViewVisits.add(new KabanViewVisit("Refusés", refused, refused.size()));
                kabanViewVisits.add(new KabanViewVisit("A certifier", certifies, certifies.size()));
                kabanViewVisits.add(new KabanViewVisit("Accepté", accepted, accepted.size()));
                kabanViewVisits.add(new KabanViewVisit("A approuver", approuve, approuve.size()));
            Message msg = msgRepo.findByCode("VS007");
            log.info("kanbanview visit");    
            long endTime = System.currentTimeMillis();
            double durationSecond = (endTime - startTime) / 1000.0;
            log.info("fetch kabanview took " + durationSecond + "seconds");

            return ApiResponseHandler.generateResponseWithAlertLevel(HttpStatus.OK, true, msg, kabanViewVisits);
        }
        catch (Exception e){
            e.printStackTrace();
            log.error("Erreur lors de l'affichage du kanban view visite");
            Message msg = msgRepo.findByCode("VS008");
            return ApiResponseHandler.generateResponseWithAlertLevel(HttpStatus.OK, false, msg, null);

        }

    }


    // flemming implimented
    @GetMapping("/api/v1/visite/graphview")
    public Response getVisitCounts() {
        UUID orgId = SessionData.getOrganisationId(request);
        List<GraphView> data = visiteService.listParStatusForGraphViews(orgId);
        int[] datas = data.stream().mapToInt(GraphView::getValue).toArray();

        return new Response(data, datas);
    }

    public static class Response {
        private List<GraphView> data;
        private int[] datas;

        public Response(List<GraphView> data, int[] datas) {
            this.data = data;
            this.datas = datas;
        }

        public List<GraphView> getData() {
            return data;
        }

        public int[] getDatas() {
            return datas;
        }
    }


    // old method 
    // @GetMapping("/api/v1/visite/graphview")
    public ResponseEntity<Object> listforGraphView() {
        try {
            log.info("Graphe view visit");
            UUID orgId = SessionData.getOrganisationId(request);
            List<Object> graphViews = new ArrayList<>();
            int[] datas = new int[9];
            for (int i = 0; i < datas.length; i++) {
                datas[i] = visiteService.listParStatus(i, orgId).size();
            }
            Map<String, int[]> result = new HashMap<>();
            result.put("tab", datas);
            graphViews.add(new GraphView("maj", visiteService.listParStatus(0, orgId).size()));
            graphViews.add(new GraphView("A inspecter", visiteService.listParStatus(1, orgId).size()));
            graphViews.add(new GraphView("En cours test", visiteService.listParStatus(2, orgId).size()));
            graphViews.add(new GraphView("A signer", visiteService.listParStatus(3, orgId).size()));
            graphViews.add(new GraphView("A imprimer", visiteService.listParStatus(4, orgId).size()));
            graphViews.add(new GraphView("A enregister", visiteService.listParStatus(5, orgId).size()));
            graphViews.add(new GraphView("A certifier", visiteService.listParStatus(6, orgId).size()));
            graphViews.add(new GraphView("Accepté", visiteService.listParStatus(7, orgId).size()));
            graphViews.add(new GraphView("Refusé", visiteService.listParStatus(8, orgId).size()));
            log.info("Affichage graphview view visite");
            Message msg = msgRepo.findByCode("VS009");
            return ApiResponseHandler.generateResponses(HttpStatus.OK, true, msg, graphViews, datas);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Erreur lors de l'affichage du graphview view visite");
            Message msg = msgRepo.findByCode("VS010");
            return ApiResponseHandler.generateResponseWithAlertLevel(HttpStatus.OK, false, msg, null);
        }

    }


    @Transactional
    @GetMapping("/api/v1/all/visite/listview")
    public ResponseEntity<Object> listforlistView() {
try{
        log.info("list view visit");
        List<Listview> listVisit = new ArrayList<>();
        UUID orgId = SessionData.getOrganisationId(request);
        for (Visite visite : visiteService.listParStatus(0, orgId)) {
            Listview lv = new Listview(visite, visiteService,gieglanFileService,catSer);
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
                lv.setCreatedAt(visite.getCreatedDate());
                lv.setReference(visite.getCarteGrise().getNumImmatriculation());
                lv.setStatut(visite.statutRender(visite.getStatut()));
                lv.setType(visite.typeRender());
                listVisit.add(lv);
                lv.setId(visite.getIdVisite());

            }
            log.info("Affichage de la liste des visites");
            Message msg = msgRepo.findByCode("VS001");
            return ApiResponseHandler.generateResponseWithAlertLevel(HttpStatus.OK, true, msg, listVisit);
        }
        catch (Exception e){
            log.info("Affichage de toutes les visite");
            Message msg = msgRepo.findByCode("VS002");
            return ApiResponseHandler.generateResponseWithAlertLevel(HttpStatus.OK, false, msg, null);
        }


    }

    @Transactional
    @GetMapping("/api/v1/visites/imprimer/pv/{visiteId}")
    public ResponseEntity<Object> printPV(@PathVariable UUID visiteId) throws ImpressionException, IOException, DocumentException {

        try{
            long startTime = System.currentTimeMillis();

            File f= new File(environment.getProperty("pv.path"));
            if(!f.exists())
                f.mkdirs();
            Visite visite = visiteService.findById(visiteId);
            String outputFolder = environment.getProperty("pv.path") + File.separator + visite.getProcess().getReference() + ".pdf";


            OutputStream outputStream =  new FileOutputStream(outputFolder);
            
            // createWatermark(visiteService.findById(visiteId).getProcess().getReference());
            CompletableFuture<Void> watermark = createWatermarkAsync(visiteService.findById(visiteId).getProcess().getReference());
            watermark.join(); 
            
            ITextRenderer renderer = new ITextRenderer();
            renderer.getSharedContext().setReplacedElementFactory(new MediaReplacedElementFactory(renderer.getSharedContext().getReplacedElementFactory()));
            renderer.setDocumentFromString(fillHtmlToValue(visiteId));
            renderer.layout();
            renderer.createPDF(outputStream);
            log.info("PDF successfully created! We thank you.");

        /*try {}catch (Exception e){
            log.info("Error occurred during pdf printing");
            log.error(e.getMessage());
        }finally {
            outputStream.close();
        }*/
            if(visite.getProcess().isStatus()){
                visite.setStatut(8);
                visite.setEncours(false);
            }
            else{
                visite.setStatut(7);
                visite.setEncours(false);
            }

           Visite visite2 = visiteService.add(visite);
            visite.getOrganisation().getUtilisateurs().forEach(utilisateur -> {
                notificationService.dipatchVisiteToMember(utilisateur.getKeycloakId(), visite2, true);
            });
            //Openaplr to know if the car is in the center
       /* String uri = environment.getProperty("endpoint.openalpr") ;
        String apiKey = environment.getProperty("endpoint.openalpr.api.key") ;

        BestPlate bestPlate = openAlprService.getPresenceConfidence(uri,apiKey,visite.getInspection());
        visite.getInspection().setDistancePercentage(
                bestPlate.getRate()
        );
        visite.getInspection().setBestPlate(bestPlate.getPlate());*/
            //visiteService.add(visite);
            log.info("PDF SUCCESSFULLY PRINTED");
            long endTime = System.currentTimeMillis();
            double durationSecond = (endTime - startTime) / 1000.0;
            log.info("pdf generation took " + durationSecond + "seconds");

            Message msg = msgRepo.findByCode("PV001");
            return ApiResponseHandler.generateResponseWithAlertLevel(HttpStatus.OK, true, msg, "/public/pv/"+visite2.getProcess().getReference()+".pdf");

        }catch (Exception e){
            e.printStackTrace();
            Message msg = msgRepo.findByCode("PV002");
            return ApiResponseHandler.generateResponseWithAlertLevel(HttpStatus.OK, true, msg, "VS002");
        }


    }   
    
    
    
    
    public String fillHtmlToValue(UUID id) {
    // public String fillHtmlToValue(UUID id) {
        Optional<Visite> visite = this.visiteRepo.findById(id);
        log.info("visite data " + visite.get().toString());

        long startTime1 = System.currentTimeMillis();
        Taxe tp = taxeService.findByNom("TVA");
        long endTime2 = System.currentTimeMillis();
        double durationSecond2 = (endTime2 - startTime1) / 1000.0;
        log.info("query for taxes took " + durationSecond2 + "seconds");

        if(visite.isPresent()) {
            long startTime2 = System.currentTimeMillis();
            List<RapportDeVisite> rapports = this.rapportDeVisiteRepo.getRapportDeVisite(visite.get());
            long endTime3 = System.currentTimeMillis();
            double durationSecond3 = (endTime3 - startTime2) / 1000.0;
            log.info("query for rapports took " + durationSecond3 + "seconds");

            log.info("current rapport size" +  rapports.size());
            long startTime23 = System.currentTimeMillis();

            Visite visitemain = visite.get();

            if(visitemain.isContreVisite()){
                log.info("processing control visite");
                List<Visite> visites = visiteService.getLastVisiteWithTestIsOkDirectQuery(visitemain.getControl().getId(), visitemain.getIdVisite());
                visites.forEach(visite1 -> {
                    List<RapportDeVisite> rapportDeVisite = visite1.getRapportDeVisites();
                    if(rapportDeVisite.isEmpty()){
                        log.info("no rapport de visite found for visite");
                    }
                    else{
                        rapports.addAll(rapportDeVisite);
                    }
                });
                log.info("visite query service size" + visites.size());
            }


           
            // this.visiteRepo.getLastVisiteWithTestIsOk(visite.get().getControl(), visite.get())
            //     .forEach(visite1 -> 
            //      rapports.addAll(visite1.getRapportDeVisites()));
            long endTime33 = System.currentTimeMillis();
            double durationSecond33 = (endTime33 - startTime23) / 1000.0;
            log.info("query for  get last rapports took " + durationSecond33 + "seconds");
            log.info("current rapport visite" +  rapports.size());

            List<Lexique> minorDefault = new ArrayList<>();
            List<Lexique> majorDefault = new ArrayList<>();
            Context context = new Context();

            rapports.forEach(rapport -> {
                String index = rapport.getSeuil().getFormule().getMesures().stream().findFirst().get().getCode();
                log.info("code " + index);
                context.setVariable("r"+index, Double.valueOf(rapport.getResult()));
                log.info("rapport result " + Double.valueOf(rapport.getResult()));

                if (rapport.getSeuil().getLexique() != null &&
                    "majeure".equalsIgnoreCase(rapport.getSeuil().getLexique().getClassification()==null ?
                        null : rapport.getSeuil().getLexique().getClassification().getCode()))
                    majorDefault.add(rapport.getSeuil().getLexique());
                else if (rapport.getSeuil().getLexique() != null &&
                    "mineure".equalsIgnoreCase(rapport.getSeuil().getLexique().getClassification()==null ?
                        null : rapport.getSeuil().getLexique().getClassification().getCode()))
                    minorDefault.add(rapport.getSeuil().getLexique());
            });

            visitemain.getInspection().getLexiques().forEach(lexique -> {
                log.info("Lexique" +  lexique);
                if ("majeure".equalsIgnoreCase(lexique.getClassification().getCode()))
                    majorDefault.add(lexique);
                else
                    minorDefault.add(lexique);
            });

            // UserDTO user = UserInfoIn.getInfosControleur(visitemain.getInspection().getControleur(), environment);
            UserDTO user = UserInfoIn.getUserInfo(request);

            ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
            templateResolver.setSuffix(".html");
            templateResolver.setTemplateMode(TemplateMode.HTML);

            TemplateEngine templateEngine = new TemplateEngine();
            templateEngine.setTemplateResolver(templateResolver);

            long startTime5 = System.currentTimeMillis();
            VisiteDate v = new VisiteDate(visite.get());
            List<MesureVisuel> mesureVisuels = mesureVisuelRepository.getMesureVisuelByInspection(
                v.getInspection(),
                PageRequest.of(0,1)
            );

            log.info("measure visuel: " + mesureVisuels);

            long endTime6 = System.currentTimeMillis();
            double durationSecond7 = (endTime6 - startTime5) / 1000.0;
            log.info("query for measure took " + durationSecond7 + "seconds");

            HashMap<String, String> phareTest = new HashMap<String, String>();
            phareTest.put("0","Trop haut / Too high");
            phareTest.put("1","Correct");
            phareTest.put("2","Trop bas / Too low");
            phareTest.put("9","-");

            Produit prod = v.getCarteGrise().getProduit();

            context.setVariable("phareTest", phareTest);
            context.setVariable("controlValidityAt", v.getControl().getValidityAt() == null ? null : convert(v.getControl().getValidityAt() ));
            context.setVariable("controlDelayAt", convert(v.getControl().getContreVDelayAt() == null
                    ? LocalDateTime.now(): v.getControl().getContreVDelayAt() ));
            context.setVariable("mesurevisuel", mesureVisuels.isEmpty() ? null : mesureVisuels.get(0));
            context.setVariable("v", v);
            context.setVariable("tp", tp);
            context.setVariable("prod", prod);
            context.setVariable("owner", v.getCarteGrise().getProprietaireVehicule().getPartenaire().getNom() +' '+
                    (v.getCarteGrise().getProprietaireVehicule().getPartenaire().getPrenom() == null? "": v.getCarteGrise().getProprietaireVehicule().getPartenaire().getPrenom()));

            context.setVariable("minorDefault", minorDefault);
            context.setVariable("majorDefault", majorDefault);
            context.setVariable("controlleurName", user.getNom() + " " + user.getPrenom());
            context.setVariable("gps", mesureVisuels.isEmpty() ? null : mesureVisuels.get(0).getGps());
            context.setVariable("o", v.getOrganisation()); 

            return templateEngine.process("templates/visites", context);
        }

        return null;

    }
    


    @Async("taskExecutorForHeavyTasks")
    public CompletableFuture<Void> createWatermarkAsync(String ref) {
        return CompletableFuture.runAsync(() -> {
            long startTime = System.currentTimeMillis();
            log.info("processing water mark by" + Thread.currentThread().getName());
            StringBuilder bld = new StringBuilder();
            for (int i = 0; i < 3500; ++i) {
                if (i % 25 == 0)
                    bld.append(ref + "\n\n\n\n");
                else
                    bld.append(ref + " ");
            }
            String modifiedRef = bld.toString(); // Create a new variable and assign ref to it
    
            BufferedImage img = new BufferedImage(2995, 2042, BufferedImage.TYPE_INT_ARGB);
    
            Graphics2D g2d = img.createGraphics();
    
            AffineTransform affineTransform = new AffineTransform();
            g2d.setTransform(affineTransform);
            g2d.rotate(-Math.PI / 10);
            Font font = new Font("Arial", Font.PLAIN, 3);
    
            g2d.setColor(Color.YELLOW);
    
            CompletableFuture<Void> drawFuture = drawParagraphAsync(g2d, modifiedRef, 1500);
            drawFuture.join(); /// Use the modifiedRef variable
            
            g2d.setFont(font);
    
            g2d.dispose();
            try {
                ImageIO.write(img, "png", new File("watermark.png"));
                long endTime = System.currentTimeMillis();
                double durationSecond = (endTime - startTime) / 1000.0;
                log.info("water mark took" + durationSecond + "seconds");
            } catch (IOException ex) {
                log.error(ex.getMessage());
            }
        });
    }
    

    @Async("taskExecutorForHeavyTasks")
    public CompletableFuture<Void> drawParagraphAsync(Graphics2D g, String paragraph, float width) {
        return CompletableFuture.runAsync(() -> {
            long startTime = System.currentTimeMillis();
            log.info("processing draw graph by" + Thread.currentThread().getName());
            LineBreakMeasurer linebreaker = new LineBreakMeasurer(new AttributedString(paragraph)
                    .getIterator(), g.getFontRenderContext());
    
            int y = 0;
            while (linebreaker.getPosition() < paragraph.length()) {
                TextLayout textLayout = linebreaker.nextLayout(width);
    
                y += textLayout.getAscent();
                y += textLayout.getAscent();
                y += textLayout.getAscent();
                y += textLayout.getAscent();
                y += textLayout.getAscent();
    
                textLayout.draw(g, -785, y);
                y += textLayout.getDescent() + textLayout.getLeading();
                y += textLayout.getDescent() + textLayout.getLeading();
                y += textLayout.getDescent() + textLayout.getLeading();
                y += textLayout.getDescent() + textLayout.getLeading();
                y += textLayout.getDescent() + textLayout.getLeading();
            }
            long endTime = System.currentTimeMillis();
            double durationSecond = (endTime - startTime) / 1000.0;
            log.info("draw graph took " + durationSecond + "seconds");
        });
    }
    
    
    


    public void createWatermark(String ref){
        long startTime = System.currentTimeMillis();
        StringBuilder bld = new StringBuilder();
        for (int i = 0; i < 3500; ++i) {
            if(i % 25 == 0)
                bld.append(ref + "\n\n\n\n");
            else
                bld.append(ref + " ");
        }
        ref =  bld.toString();

        BufferedImage img = new BufferedImage(2995, 2042, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = img.createGraphics();


        AffineTransform affineTransform = new AffineTransform();
        g2d.setTransform(affineTransform);
        g2d.rotate(-Math.PI/10);
        Font font = new Font("Arial", Font.PLAIN, 3);

        g2d.setColor(Color.YELLOW);


        drawParagraph(g2d, ref, 1500);
        g2d.setFont(font);

        g2d.dispose();
        try {
            ImageIO.write(img, "png", new File("watermark.png"));
            long endTime = System.currentTimeMillis();
            double durationSecond = (endTime - startTime) / 1000.0;
            log.info("water mark took " + durationSecond + "seconds");
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
    }

    
    void drawParagraph(Graphics2D g, String paragraph, float width) {
        long startTime = System.currentTimeMillis();
        LineBreakMeasurer linebreaker = new LineBreakMeasurer(new AttributedString(paragraph)
                .getIterator(), g.getFontRenderContext());

        int y = 0;
        while (linebreaker.getPosition() < paragraph.length()) {
            TextLayout textLayout = linebreaker.nextLayout(width);

            y += textLayout.getAscent();
            y += textLayout.getAscent();
            y += textLayout.getAscent();
            y += textLayout.getAscent();
            y += textLayout.getAscent();

            textLayout.draw(g, -785, y);
            y += textLayout.getDescent() + textLayout.getLeading();
            y += textLayout.getDescent() + textLayout.getLeading();
            y += textLayout.getDescent() + textLayout.getLeading();
            y += textLayout.getDescent() + textLayout.getLeading();
            y += textLayout.getDescent() + textLayout.getLeading();
        }
            long endTime = System.currentTimeMillis();
            double durationSecond = (endTime - startTime) / 1000.0;
            log.info("draw graph took " + durationSecond + "seconds");
    }

   
    public Date convert(LocalDateTime dateToConvert) {
        return Date
                .from(
                        dateToConvert
                                .atZone(
                                        ZoneId
                                                .systemDefault())
                        .toInstant());
    }


    @Transactional
    @PostMapping(path = "/api/v1/visite/conformity/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Object checkCconformity(
            @PathVariable Long id,
            @RequestParam("files") MultipartFile[] files,
            @RequestParam("data") String data
    )  throws JsonProcessingException {

            ObjectMapper objectMapper = new ObjectMapper();
            DataRapportDto dataRapportDto = objectMapper.readValue(data, DataRapportDto.class);
            List<String> fileNames = new ArrayList<>();

            Arrays.asList(files).stream().forEach(file -> {
                storageService.save(file);
                fileNames.add(file.getOriginalFilename());
            });

            String endPoint = environment.getProperty("endpoint.check-conformity");

            HttpEntity<DataRapportDto> requestt = new HttpEntity<>(dataRapportDto);

            ResponseEntity<String> response = (new RestTemplate()).postForEntity(
                    endPoint+id,
                    requestt,
                    String.class
            );


            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "OK", response);



    }

    @Transactional
    @GetMapping(value = "/api/v1/visite/tests/{i}")
    public ResponseEntity<Object> getInspectionTest(@PathVariable UUID i) {

        try {

            Inspection inspection = inspectionService.findInspectionById(i);
            List<Testdto> files = new ArrayList<>();
            if(inspection.getVisite().isContreVisite()){
                files = gieglanFileService.getGieglanFileFailed(inspection.getVisite())
                        .stream()
                        .map(gieglanFile -> new Testdto(gieglanFile.getCategorieTest().getLibelle(), gieglanFile.getCategorieTest().getDescription()))
                        .collect(Collectors.toList());
            }
            else{
                files = inspection.getVisite().getCarteGrise()
                        .getProduit().getCategorieTestProduits()
                        .stream()
                        .map(categorieTestProduit -> new Testdto(categorieTestProduit.getCategorieTest().getLibelle(), categorieTestProduit.getCategorieTest().getDescription()))
                        .collect(Collectors.toList());
            }

            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "tests", files);
        } catch (Exception e) {
            return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, "tests", null);
        }

    }



    @Transactional
    @PostMapping("/api/v1/visite/{id}/status/{status}")
    public ResponseEntity<Object> editStatus(@PathVariable UUID id, @PathVariable int status) {

        Visite v = visiteService.findById(id);
        v.setStatut(status);
        v = visiteService.add(v);

        return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "OK", v);
    }

    public String getHTML(Visite visite) {
        String icons = "";
        switch (visite.statutRender()) {
            case "maj":
                return "<span class=\"badge badge-primary\">" + visite.statutRender() + "</span>";

            case "A inspecter":
                return  "<span class=\"badge badge-warning\">" + visite.statutRender() + "</span>";

            case "En cours test":
                for (GieglanFileIcon cat : replaceIconIfNecessary(visite)) {
                    icons += cat.getIcon();
                }
                return icons;

            case "A signer":
                if(visite.getProcess().isStatus())
                    return  "<span class=\"badge badge-info\"> ACCEPTE " + visite.statutRender() + "</span>";
                else
                    return  "<span class=\"badge badge-info\"> REFUSE " + visite.statutRender() + "</span>";

            case "A imprimer":
                    return "<span class=\"badge badge-success\">" + visite.statutRender() + "</span>";

            case "A certifier":
                    return "<span class=\"badge badge-primary\">" + visite.statutRender() + "</span>";
            case "Accepté":
                    return "<span class=\"badge badge-success\">" + visite.statutRender() + "</span>";
            case "Refusé":
                    return "<span class=\"badge badge-dark\">" + visite.statutRender() + "</span>";
            default:
                    return "<span class=\"badge badge-warning\">" + visite.statutRender() + "</span>";
        }
    }

    public List<GieglanFileIcon> replaceIconIfNecessary(Visite visite){
        System.out.println("build visite +++++++++++++++"+ visite.getIdVisite());
        ProduitCategorieTest p = Utils.tests.stream()
                .filter(produitCategorieTest -> produitCategorieTest.getProduitId().equals(visite.getCarteGrise().getProduit().getProduitId()))
                .findFirst()
                .get();
        Set<GieglanFileIcon> icons = new HashSet<>();
        // List<GieglanFileIcon> icons =new ArrayList<>();
        List<GieglanFileIcon> gieglanFileIcons  =p.getTest().stream().map(
                testNew -> new GieglanFileIcon(testNew.getExtension(), testNew.getIcon())
        ).collect(Collectors.toList());
        if(visite.isContreVisite()){
            gieglanFileService.getGieglanFileFailed(visite).forEach(g -> {
                switch (g.getCategorieTest().getLibelle()){
                    case "F" :
                        if(visite.getFreinage().equals(Visite.TestResult.PENDING))
                            icons.add(new GieglanFileIcon("F", "<span class=\"badge badge-light\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Freinage\"><i class=\"i-Pause\"></i></span>&nbsp"));
                        else if(visite.getFreinage().equals(Visite.TestResult.SUCCESS))
                            icons.add(new GieglanFileIcon("F", "<span class=\"badge badge-success\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Freinage\"><i class=\"i-Pause\"></i></span>&nbsp"));
                        else
                            icons.add(new GieglanFileIcon("F", "<span class=\"badge badge-danger\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Freinage\"><i class=\"i-Pause\"></i></span>&nbsp"));
                        break;
                    case "R" :
                        if(visite.getRipage().equals(Visite.TestResult.PENDING))
                            icons.add(new GieglanFileIcon("R", "<span class=\"badge badge-light\"><i class=\"i-Car-2\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Ripage\"></i></span>&nbsp"));
                        else if(visite.getRipage().equals(Visite.TestResult.SUCCESS))
                            icons.add(new GieglanFileIcon("R", "<span class=\"badge badge-success\"><i class=\"i-Car-2\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Ripage\"></i></span>&nbsp"));
                        else
                            icons.add(new GieglanFileIcon("R", "<span class=\"badge badge-danger\"><i class=\"i-Car-2\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Ripage\"></i></span>&nbsp"));
                        break;
                    case "S" :
                        if(visite.getSuspension().equals(Visite.TestResult.PENDING))
                            icons.add(new GieglanFileIcon("S", "<span class=\"badge badge-light\"><i class=\"i-Jeep-2\"  data-toggle=\"tooltip\" data-placement=\"top\" title=\"Suspension\"></i></span>&nbsp"));
                        else if(visite.getSuspension().equals(Visite.TestResult.SUCCESS))
                            icons.add(new GieglanFileIcon("S", "<span class=\"badge badge-success\"><i class=\"i-Jeep-2\"  data-toggle=\"tooltip\" data-placement=\"top\" title=\"Suspension\"></i></span>&nbsp"));
                        else
                            icons.add(new GieglanFileIcon("S", "<span class=\"badge badge-danger\"><i class=\"i-Jeep-2\"  data-toggle=\"tooltip\" data-placement=\"top\" title=\"Suspension\"></i></span>&nbsp"));
                        break;
                    case "P" :
                        if(visite.getReglophare().equals(Visite.TestResult.PENDING))
                            icons.add(new GieglanFileIcon("P", "<span class=\"badge badge-light\"><i class=\"i-Flash\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Réglophare\"></i></span>&nbsp"));
                        else if(visite.getReglophare().equals(Visite.TestResult.SUCCESS))
                            icons.add(new GieglanFileIcon("P", "<span class=\"badge badge-success\"><i class=\"i-Flash\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Réglophare\"></i></span>&nbsp"));
                        else
                            icons.add(new GieglanFileIcon("P", "<span class=\"badge badge-danger\"><i class=\"i-Flash\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Réglophare\"></i></span>&nbsp"));
                        break;

                    case "JSON":

                        if(visite.getVisuel().equals(Visite.TestResult.PENDING))
                            icons.add(new GieglanFileIcon("JSON", "<span class=\"badge badge-light\"><i class=\"i-Eye\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Mesures visuelles\"></i></span>&nbsp"));
                        else if(visite.getVisuel().equals(Visite.TestResult.SUCCESS))
                            icons.add(new GieglanFileIcon("JSON", "<span class=\"badge badge-success\"><i class=\"i-Eye\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Mesures visuelles\"></i></span>&nbsp"));
                        else
                            icons.add(new GieglanFileIcon("JSON", "<span class=\"badge badge-danger\"><i class=\"i-Eye\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Mesures visuelles\"></i></span>&nbsp"));
                        break;
                    case "G":
                        if(visite.getPollution().equals(Visite.TestResult.PENDING))
                            icons.add(new GieglanFileIcon("G", "<span class=\"badge badge-light\"><i class=\"i-Cloud1\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Analyseur de gaz\"></i></span>&nbsp"));
                        else if(visite.getPollution().equals(Visite.TestResult.SUCCESS))
                            icons.add(new GieglanFileIcon("G", "<span class=\"badge badge-success\"><i class=\"i-Cloud1\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Analyseur de gaz\"></i></span>&nbsp"));
                        else
                            icons.add(new GieglanFileIcon("G", "<span class=\"badge badge-danger\"><i class=\"i-Cloud1\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Analyseur de gaz\"></i></span>&nbsp"));

                }
        });

        }

        else {
            gieglanFileIcons.forEach(gieglanFileIcon -> {
                switch (gieglanFileIcon.getExtension()){
                    case "F" :
                        if(visite.getFreinage().equals(Visite.TestResult.PENDING))
                            icons.add(new GieglanFileIcon("F", "<span class=\"badge badge-light\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Freinage\"><i class=\"i-Pause\"></i></span>&nbsp"));
                        else if(visite.getFreinage().equals(Visite.TestResult.SUCCESS))
                            icons.add(new GieglanFileIcon("F", "<span class=\"badge badge-success\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Freinage\"><i class=\"i-Pause\"></i></span>&nbsp"));
                        else
                            icons.add(new GieglanFileIcon("F", "<span class=\"badge badge-danger\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Freinage\"><i class=\"i-Pause\"></i></span>&nbsp"));
                        break;
                    case "R" :
                        if(visite.getRipage().equals(Visite.TestResult.PENDING))
                            icons.add(new GieglanFileIcon("R", "<span class=\"badge badge-light\"><i class=\"i-Car-2\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Ripage\"></i></span>&nbsp"));
                        else if(visite.getRipage().equals(Visite.TestResult.SUCCESS))
                            icons.add(new GieglanFileIcon("R", "<span class=\"badge badge-success\"><i class=\"i-Car-2\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Ripage\"></i></span>&nbsp"));
                        else
                            icons.add(new GieglanFileIcon("R", "<span class=\"badge badge-danger\"><i class=\"i-Car-2\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Ripage\"></i></span>&nbsp"));
                        break;
                    case "S" :
                        if(visite.getSuspension().equals(Visite.TestResult.PENDING))
                            icons.add(new GieglanFileIcon("S", "<span class=\"badge badge-light\"><i class=\"i-Jeep-2\"  data-toggle=\"tooltip\" data-placement=\"top\" title=\"Suspension\"></i></span>&nbsp"));
                        else if(visite.getSuspension().equals(Visite.TestResult.SUCCESS))
                            icons.add(new GieglanFileIcon("S", "<span class=\"badge badge-success\"><i class=\"i-Jeep-2\"  data-toggle=\"tooltip\" data-placement=\"top\" title=\"Suspension\"></i></span>&nbsp"));
                        else
                            icons.add(new GieglanFileIcon("S", "<span class=\"badge badge-danger\"><i class=\"i-Jeep-2\"  data-toggle=\"tooltip\" data-placement=\"top\" title=\"Suspension\"></i></span>&nbsp"));
                        break;
                    case "P" :
                        if(visite.getReglophare().equals(Visite.TestResult.PENDING))
                            icons.add(new GieglanFileIcon("P", "<span class=\"badge badge-light\"><i class=\"i-Flash\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Réglophare\"></i></span>&nbsp"));
                        else if(visite.getReglophare().equals(Visite.TestResult.SUCCESS))
                            icons.add(new GieglanFileIcon("P", "<span class=\"badge badge-success\"><i class=\"i-Flash\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Réglophare\"></i></span>&nbsp"));
                        else
                            icons.add(new GieglanFileIcon("P", "<span class=\"badge badge-danger\"><i class=\"i-Flash\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Réglophare\"></i></span>&nbsp"));
                        break;

                    case "JSON":

                        if(visite.getVisuel().equals(Visite.TestResult.PENDING))
                            icons.add(new GieglanFileIcon("JSON", "<span class=\"badge badge-light\"><i class=\"i-Eye\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Mesures visuelles\"></i></span>&nbsp"));
                        else if(visite.getVisuel().equals(Visite.TestResult.SUCCESS))
                            icons.add(new GieglanFileIcon("JSON", "<span class=\"badge badge-success\"><i class=\"i-Eye\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Mesures visuelles\"></i></span>&nbsp"));
                        else
                            icons.add(new GieglanFileIcon("JSON", "<span class=\"badge badge-danger\"><i class=\"i-Eye\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Mesures visuelles\"></i></span>&nbsp"));
                        break;
                    case "G":
                        if(visite.getPollution().equals(Visite.TestResult.PENDING))
                            icons.add(new GieglanFileIcon("G", "<span class=\"badge badge-light\"><i class=\"i-Cloud1\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Analyseur de gaz\"></i></span>&nbsp"));
                        else if(visite.getPollution().equals(Visite.TestResult.SUCCESS))
                            icons.add(new GieglanFileIcon("G", "<span class=\"badge badge-success\"><i class=\"i-Cloud1\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Analyseur de gaz\"></i></span>&nbsp"));
                        else
                        icons.add(new GieglanFileIcon("G", "<span class=\"badge badge-danger\"><i class=\"i-Cloud1\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Analyseur de gaz\"></i></span>&nbsp"));
                    break;
                }
            });


        }
        return new ArrayList<>(icons);
        // return icons;

    }


    /****Administration****/

    @Transactional
    @GetMapping(value = "/api/v1/admin/visites",  params = { "search","page", "size" })
    public ResponseEntity<Object> getAllActive(@RequestParam(value = "search", required = false, defaultValue = "")  final String search,
                                                @RequestParam("page") int page,
                                               @RequestParam("size") int size) {
        // List<Listview> listVisit = new ArrayList<>();

        List<Visite> resultPage = visiteService.visitListForAdmin(search, PageRequest.of(page, size));
        List<NewListView> newListViews = resultPage.stream().map(visite ->
                new NewListView(visite.getIdVisite(),visite.getCarteGrise().getProduit(),visite.typeRender(), visite.getCarteGrise().getNumImmatriculation(),
                        (visite.getCarteGrise().getVehicule()==null
                                ? "": (visite.getCarteGrise().getVehicule().getChassis()==null
                                ? "" : visite.getCarteGrise().getVehicule().getChassis())),
                        (visite.getVente() ==null ? "" :(visite.getVente().getClient() == null
                                ? visite.getVente().getContact().getPartenaire().getNom() : visite.getVente().getClient().getPartenaire().getNom())),
                        Utils.parseDate(visite.getCreatedDate()), visite.getCreatedDate(),
                        getHTML(visite), visite.getStatut(), visite.getIdVisite(),visite.isContreVisite(),
                        visite.getInspection()==null? null : visite.getInspection().getIdInspection(), visite.getCarteGrise(), visite.getOrganisation().isConformity(),
                        visite.getIsConform(),
                        visite.getOrganisation().getNom() ,visite.getInspection()==null? null : visite.getInspection().getBestPlate(), visite.getInspection()==null? 0 : visite.getInspection().getDistancePercentage(),
                        visite.getCreatedDate().format(SseController.dateTimeFormatter), false, visite.getDocument())
        ).collect(Collectors.toList());
        Page<NewListView> pages = new PageImpl<>(newListViews, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id")),300);
        PagedModel<EntityModel<NewListView>> result = pagedResourcesAssembler
                .toModel(pages);
        return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "OK", result);
    }


    @PostMapping(value = "/api/v1/admin/visites")
    public ResponseEntity<Object> saveVisite(@RequestBody VisitePOJO visitePOJO) {

        Visite v = visitePOJO.getIdVisite() == null ? new Visite() : visiteService.findById(visitePOJO.getIdVisite());

        v.setContreVisite(visitePOJO.isContreVisite());
        v.setEncours(visitePOJO.isEncours());
        v.setStatut(Integer.valueOf(visitePOJO.getStatut()));
        v.setStatut(v.getStatut());
        v.setIdVisite(visitePOJO.getIdVisite());
        v = visiteService.add(v);

        return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "OK", v);
    }


    @Transactional
    @PostMapping(value = "/api/v1/admin/visites/reset/{id}")
    public ResponseEntity<Object> reset(@PathVariable UUID id) {

        Visite v = visiteService.findById(id);

        v.setStatut(1);
        gieglanFileService.findActiveByInspection(v.getInspection().getIdInspection())
            .forEach(gieglanFile -> {
                gieglanFile.setActiveStatus(false);
                gieglanFile.getValeurTests().forEach(valeurTest ->
                    valeurTest.setActiveStatus(false)
                );
                gieglanFile.getRapportDeVisites().forEach(rapportDeVisite ->
                    rapportDeVisite.setActiveStatus(false)
                );
                if(gieglanFile.getMesureVisuel() != null){
                    gieglanFile.getMesureVisuel().setGieglanFileDeleted(gieglanFile.getId());
                    gieglanFile.getMesureVisuel().setGieglanFile(null);
                    gieglanFile.getMesureVisuel().setActiveStatus(false);
                }

            });
        v.getInspection().getLexiques().forEach(lexique ->
                lexique.setActiveStatus(false)
            );
        v.getInspection().setVisiteIdReseted(v.getIdVisite().toString());

        v = visiteService.add(v);

        return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "OK", v);
    }

    @GetMapping(value = "/api/v1/admin/visites/{id}")
    public ResponseEntity<Object> getOneVisite(@PathVariable UUID id) {

        Visite visite = visiteService.findById(id);

        return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "OK", visite);

    }

    @Transactional
    @DeleteMapping(value = "/api/v1/admin/visites/{id}")
    public ResponseEntity<Object> delVisite(@PathVariable UUID id) {

        Visite visite = visiteService.findById(id);
        visite.getVente().getDetailventes().forEach(
                detailVente -> detailVente.setActiveStatus(false)
        );
        visite.getVente().setActiveStatus(false);
        visite.setActiveStatus(false);
        visiteService.add(visite);

        return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "OK", visite);

    }
    @GetMapping(value = "/api/v1/admin/visites/today")
    public ResponseEntity<Object> getTodayVisite() {

        int size = visiteService.getVisitsOfTheDay();

        return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "OK", size);

    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VisiteController)) return false;
        VisiteController that = (VisiteController) o;
        return Objects.equals(environment, that.environment) &&
                Objects.equals(request, that.request) &&
                Objects.equals(visiteRepo, that.visiteRepo) &&
                Objects.equals(openAlprService, that.openAlprService) &&
                Objects.equals(messageRepository, that.messageRepository) &&
                Objects.equals(rapportListService, that.rapportListService) &&
                Objects.equals(pdfGenaratorUtil, that.pdfGenaratorUtil) &&
                Objects.equals(visiteService, that.visiteService) &&
                Objects.equals(rapportDeVisiteRepo, that.rapportDeVisiteRepo) &&
                Objects.equals(mesureVisuelRepository, that.mesureVisuelRepository) &&
                Objects.equals(venteService, that.venteService) &&
                Objects.equals(taxeService, that.taxeService) &&
                Objects.equals(storageService, that.storageService) &&
                Objects.equals(ps, that.ps) &&
                Objects.equals(gieglanFileService, that.gieglanFileService) &&
                Objects.equals(catSer, that.catSer) &&
                Objects.equals(pagedResourcesAssembler, that.pagedResourcesAssembler);
    }

    @Override
    public int hashCode() {

        return Objects.hash(environment, request, visiteRepo, openAlprService, messageRepository, rapportListService, pdfGenaratorUtil, visiteService, rapportDeVisiteRepo, mesureVisuelRepository, venteService, taxeService, storageService, ps, gieglanFileService, catSer, pagedResourcesAssembler);
    }
}
