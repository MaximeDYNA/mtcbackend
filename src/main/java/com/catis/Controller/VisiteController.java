package com.catis.Controller;

import java.io.*;
import java.time.*;

import java.util.Date;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;

import com.catis.Controller.configuration.SessionData;
import com.catis.Controller.pdfhandler.PdfGenaratorUtil;
import com.catis.Event.VisiteCreatedEvent;
import com.catis.model.entity.*;
import com.catis.objectTemporaire.*;
import com.catis.repository.*;
import com.catis.service.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.*;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import javax.servlet.http.HttpServletRequest;
import javax.swing.text.html.ListView;

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
    private CaissierService caissierService;
    @Autowired
    private OrganisationService os;

    @Autowired
    MessageRepository messageRepository;

    @Autowired FindRapportListService rapportListService;

    @Autowired
    PdfGenaratorUtil pdfGenaratorUtil;

    @Autowired
    private VisiteService visiteService;

    @Autowired
    private RapportDeVisiteRepo rapportDeVisiteRepo;

    @Autowired
    private MesureVisuelRepository mesureVisuelRepository;
    @Autowired
    private PdfService pdfService;
    @Autowired
    private InspectionService inspectionService;
    @Autowired
    private VenteService venteService;
    @Autowired
    private TaxeService taxeService;

    @Autowired
    FilesStorageService storageService;

    @Autowired
    private VisiteService vs;
    @Autowired
    private ProduitService ps;

    @Autowired
    private CarteGriseService cgs;
    @Autowired
    private GieglanFileService gieglanFileService;
    @Autowired
    private CategorieTestVehiculeService catSer;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private PagedResourcesAssembler<Listview> pagedResourcesAssembler;

    private static Logger log = LoggerFactory.getLogger(VisiteController.class);


    static List<SseEmitter> emitters= new CopyOnWriteArrayList<>();



    @GetMapping(value="/public/subscribe",consumes = MediaType.ALL_VALUE)
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
    @GetMapping(value="/api/v1/all/dispatchedit",consumes = MediaType.ALL_VALUE)
    public static void  dispatchEdit(Visite visite, VisiteService vs,
                                     GieglanFileService gieglanFileService,
                                     CategorieTestVehiculeService catSer, ProduitService ps)  {

        for(SseEmitter emitter:emitters){
            try{
                System.out.println("-----sse----");
                if(visite.getStatut()==1){
                    emitter.send(SseEmitter.event().name("edit_visit").data(
                            buildListView(visite, vs, gieglanFileService,catSer, ps)));
                    emitter.send(SseEmitter.event().name("controleur_visit").data(visite));
                }
                else{
                    Listview l = buildListView(visite, vs, gieglanFileService,catSer, ps);
                    emitter.send(SseEmitter.event().name("edit_visit").data(l));
                }


            }catch(IOException e){
                System.out.println("---SSE ERROR---");
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

    @GetMapping(value = "/api/v1/all/visitesencours", params = { "page", "size" })
    public ResponseEntity<Object> listDesVisitesEncours(@RequestParam("page") int page,
                                                        @RequestParam("size") int size) throws Exception {

        log.info("Liste des visites en cours");

        Page<Visite> resultPage = vs.enCoursVisitList(SessionData.getOrganisationId(request), PageRequest.of(page, size));//PageRequest.of(page, size)

        List<Listview> listVisit = new ArrayList<>();
        resultPage.forEach(visite -> {
            listVisit.add(buildListView(visite, vs, gieglanFileService,catSer, ps));
        });

        //convert list to page for applying hatoas
        Page<Listview> pages = new PageImpl<Listview>(listVisit, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id")), listVisit.size());
        //Page<Listview> pages = new PageImpl<>(listVisit, PageRequest.of(page, size), size);
        PagedModel<EntityModel<Listview>> result = pagedResourcesAssembler
                .toModel(pages);

        return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "OK", result);

    }

    @GetMapping(value = "/api/v1/all/visites")
    public ResponseEntity<Object> getAllAcitveViset() {

        log.info("Liste des visites en cours");
        List<Listview> listVisit = new ArrayList<>();
        vs.AllVisitList(SessionData.getOrganisationId(request)).forEach(visite -> {
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

    @RequestMapping(method = RequestMethod.GET, value = "/api/v1/all/visite/listview")
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

    @RequestMapping(method = RequestMethod.GET, value = "/api/v1/all/visite/listview/{statutCode}")
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

    @GetMapping("/api/v1/visites/imprimer/pv/{visiteId}")
    public String printPV(@PathVariable Long visiteId) throws Exception {

        log.info("Impression PV");

        File f= new File(environment.getProperty("pv.path"));
        if(!f.exists())
            f.mkdirs();

        String outputFolder = environment.getProperty("pv.path") + File.separator + visiteId.toString() + ".pdf";
        OutputStream outputStream = new FileOutputStream(outputFolder);

        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(parseThymeleafTemplate(visiteId));
        renderer.layout();
        renderer.createPDF(outputStream);

        outputStream.close();

            Visite visite = vs.findById(visiteId);
            if(visite.getProcess().isStatus()){
                visite.setStatut(6);
            }
            else{
                visite.setStatut(5);
                visite.setEncours(false);
            }

            visite = vs.add(visite);
        applicationEventPublisher.publishEvent(new VisiteCreatedEvent(visite));
        VisiteController.dispatchEdit(visite,
                vs, gieglanFileService, catSer, ps);
            return "/public/pv/"+visiteId+".pdf";
        /*try {} catch (Exception e) {
            log.error("Erreur lors de l'impression du PV");
            return "<h1> Erreur lors l'impression du PV </h1>";
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
        Listview v = new Listview(visite.getIdVisite(), vs, gieglanFileService,catSer);
        v.setCategorie(ps.findByImmatriculation(visite.getCarteGrise()
                .getNumImmatriculation()));

        if (visite.getCarteGrise().getProprietaireVehicule()
                .getPartenaire()
                .getNom()
                == null)
            v.setClient(null);
        else
            v.setClient(visite.getCarteGrise().getProprietaireVehicule()
                    .getPartenaire()
                    .getNom());
        v.setDate(visite.getDateDebut());
        v.setReference(visite.getCarteGrise().getNumImmatriculation());
        v.setStatut(visite.statutRender(visite.getStatut()));
        v.setType(visite.typeRender());
        return v;
    }
    public String parseThymeleafTemplate(long id) throws Exception {

        Optional<Visite> visite = this.visiteRepo.findById(id);
        Taxe tp = taxeService.findByNom("TVA");
        if (visite.isPresent() ) {
            List<RapportDeVisite> rapports = this.rapportDeVisiteRepo.getRapportDeVisite(visite.get());
            this.visiteRepo.getLastVisiteWithTestIsOk(visite.get().getControl(), visite.get())
                .forEach(visite1 -> { rapports.addAll(visite1.getRapportDeVisites()); });

            List<Lexique> minorDefault = new ArrayList<>();
            List<Lexique> majorDefault = new ArrayList<>();
            Context context = new Context();
            rapports.forEach(rapport -> {
                String index = rapport.getSeuil().getFormule().getMesures().stream().findFirst().get().getCode();
                context.setVariable("r"+index, Double.valueOf(rapport.getResult()));

                if (rapport.getSeuil().getLexique() != null &&
                    "majeure".equalsIgnoreCase(rapport.getSeuil().getLexique().getClassification()==null ?
                        null : rapport.getSeuil().getLexique().getClassification().getCode()))
                    majorDefault.add(rapport.getSeuil().getLexique());
                else if (rapport.getSeuil().getLexique() != null &&
                    "mineure".equalsIgnoreCase(rapport.getSeuil().getLexique().getClassification()==null ?
                        null : rapport.getSeuil().getLexique().getClassification().getCode()))
                    minorDefault.add(rapport.getSeuil().getLexique());
            });

            visite.get().getInspection().getLexiques().forEach(lexique -> {
                if ("majeure".equalsIgnoreCase(lexique.getClassification().getCode()))
                    majorDefault.add(lexique);
                else
                    minorDefault.add(lexique);
            });

            UserDTO user = UserInfoIn.getInfosControleur(visite.get().getInspection().getControleur(), request, environment);

            ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
            templateResolver.setSuffix(".html");
            templateResolver.setTemplateMode(TemplateMode.HTML);

            TemplateEngine templateEngine = new TemplateEngine();
            templateEngine.setTemplateResolver(templateResolver);

            VisiteDate v = new VisiteDate(visite.get());
            List<MesureVisuel> mesureVisuels = mesureVisuelRepository.getMesureVisuelByInspection(
                v.getInspection(),
                PageRequest.of(0,1)
            );

            context.setVariable("controlValidityAt", v.getControl().getValidityAt() == null ? null : convert(v.getControl().getValidityAt() ));
            context.setVariable("controlDelayAt", convert(v.getControl().getContreVDelayAt() == null
                    ? LocalDateTime.now(): v.getControl().getContreVDelayAt() ));
            context.setVariable("mesurevisuel", mesureVisuels.isEmpty() ? null : mesureVisuels.get(0));
            context.setVariable("v", v);
            context.setVariable("tp", tp);

            context.setVariable("minorDefault", minorDefault);
            context.setVariable("majorDefault", majorDefault);
            context.setVariable("controlleurName", user.getNom() + " " + user.getPrenom());
            context.setVariable("gps", mesureVisuels.isEmpty() ? null : mesureVisuels.get(0).getGps());
            context.setVariable("o", v.getOrganisation());

            return templateEngine.process("templates/visites", context);
        }

        return null;

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

    @PostMapping(path = "/api/v1/visite/conformity/{Id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Object checkCconformity(
            @PathVariable Long Id,
            @RequestParam("files") MultipartFile[] files,
            @RequestParam("data") String data
    )  throws JsonProcessingException {
        //try {
            ObjectMapper objectMapper = new ObjectMapper();
            DataRapportDto dataRapportDto = objectMapper.readValue(data, DataRapportDto.class);
            System.out.println("integer" + Id);
            List<String> fileNames = new ArrayList<>();

            Arrays.asList(files).stream().forEach(file -> {
                storageService.save(file);
                fileNames.add(file.getOriginalFilename());
            });

            String endPoint = environment.getProperty("endpoint.check-conformity");
            HttpEntity<DataRapportDto> request = new HttpEntity<>(dataRapportDto);
            ResponseEntity<String> response = (new RestTemplate()).postForEntity(
                    endPoint+Id,
                    request,
                    String.class
            );

            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "OK", dataRapportDto);


        /*} catch (HttpStatusCodeException e) {
            System.out.println("integer" + e.);
            return ApiResponseHandler.generateResponse(e.getStatusCode(), true, "", "");
        } catch (Exception e) {
            return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, true, "une erreur interne est survenue", "");

        }*/
    }




    @PostMapping("/api/v1/visite/{id}/status/{status}")
    public ResponseEntity<Object> editStatus(@PathVariable Long id, @PathVariable int status) {

        Visite v = visiteService.findById(id);
        v.setStatut(status);
        v = visiteService.add(v);

        return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "OK", v);
    }


    /****Administration****/

    @GetMapping(value = "/api/v1/admin/visites")
    public ResponseEntity<Object> getAllActive() {
        List<Listview> listVisit = new ArrayList<>();
        vs.findActiveVisites().forEach( visite -> {
            listVisit.add(buildListView(visite, vs, gieglanFileService,catSer, ps));
        });
        return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "OK", listVisit);
    }

    @PostMapping(value = "/api/v1/admin/visites")
    public ResponseEntity<Object> saveVisite(@RequestBody VisitePOJO visitePOJO) {

        Visite v = visitePOJO.getIdVisite() == null ? new Visite() : vs.findById(visitePOJO.getIdVisite());
        //LocalDateTime dated = visitePOJO.getDateDebut() == null ? null: LocalDateTime.ofInstant(Instant.ofEpochMilli(visitePOJO.getDateDebut()), ZoneId.systemDefault());
        //LocalDateTime datef = visitePOJO.getDateFin() == null ? null: LocalDateTime.ofInstant(Instant.ofEpochMilli(visitePOJO.getDateFin()), ZoneId.systemDefault());
        //Caissier caissier = visitePOJO.getCaissier() == null ? null : caissierService.findById(visitePOJO.getIdVisite());
        //CarteGrise carteGrise = visitePOJO.getCarteGrise() == null ? null : cgs.findCarteGriseById(visitePOJO.getCarteGrise().getId());
        //Organisation organisation = visitePOJO.getOrganisationId()==null ? null : os.findByOrganisationId(visitePOJO.getOrganisationId().getId());

        v.setContreVisite(visitePOJO.isContreVisite());
        v.setEncours(visitePOJO.isEncours());
        v.setStatut(Integer.valueOf(visitePOJO.getStatut()));
        //v.setCarteGrise(carteGrise);
        v.setStatut(v.getStatut());
        //v.setOrganisation(organisation);
       // v.setDateFin(datef);
        //v.setDateDebut(dated);
        //v.setCaissier(caissier);
        v.setIdVisite(visitePOJO.getIdVisite());
        v = vs.add(v);

        return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "OK", v);
    }

    @PostMapping(value = "/api/v1/admin/visites/reset/{id}")
    public ResponseEntity<Object> reset(@PathVariable Long id) {

        System.out.println("id-----------"+id);
        Visite v = visiteService.findById(id);

        v.setStatut(1);
        gieglanFileService.findActiveByInspection(v.getInspection().getIdInspection())
            .forEach(gieglanFile -> {
                gieglanFile.setActiveStatus(false);
                gieglanFile.getValeurTests().forEach(valeurTest -> {
                    valeurTest.setActiveStatus(false);
                });
                gieglanFile.getRapportDeVisites().forEach(rapportDeVisite -> {
                    rapportDeVisite.setActiveStatus(false);
                });
                if(gieglanFile.getMesureVisuel() != null){
                    gieglanFile.getMesureVisuel().setGieglanFileDeleted(gieglanFile.getId());
                    gieglanFile.getMesureVisuel().setGieglanFile(null);
                    gieglanFile.getMesureVisuel().setActiveStatus(false);
                }

            });
        v.getInspection().getLexiques().forEach(lexique -> {
                lexique.setActiveStatus(false);
            });
        v.getInspection().setVisiteIdReseted(v.getIdVisite());

        v = visiteService.add(v);

        return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "OK", v);
    }

    @GetMapping(value = "/api/v1/admin/visites/{id}")
    public ResponseEntity<Object> getOneVisite(@PathVariable Long id) {

        Visite visite = vs.findById(id);

        return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "OK", visite);

    }

    @DeleteMapping(value = "/api/v1/admin/visites/{id}")
    public ResponseEntity<Object> delVisite(@PathVariable Long id) {

        Visite visite = vs.findById(id);
        visite.setStatut(0);
        visiteService.add(visite);

        return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "OK", visite);

    }
    @GetMapping(value = "/api/v1/admin/visites/today")
    public ResponseEntity<Object> getTodayVisite() {

        int size = vs.getVisitsOfTheDay();

        return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "OK", size);

    }

}
