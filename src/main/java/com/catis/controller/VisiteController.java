package com.catis.controller;

import java.awt.*;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.AttributedString;
import java.time.*;

import java.util.Date;
import java.util.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import com.catis.controller.configuration.SessionData;
import com.catis.controller.exception.ImpressionException;
import com.catis.controller.pdfhandler.MediaReplacedElementFactory;
import com.catis.controller.pdfhandler.PdfGenaratorUtil;
import com.catis.Event.VisiteCreatedEvent;
import com.catis.model.entity.*;
import com.catis.objectTemporaire.*;
import com.catis.repository.*;
import com.catis.service.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    }

    @GetMapping(value = "/api/v1/all/visitesencours", params = { "page", "size" })
    public ResponseEntity<Object> listDesVisitesEncours(@RequestParam("page") int page,
                                                        @RequestParam("size") int size) {
        log.info("Liste des visites en cours ---");
        Long orgId = SessionData.getOrganisationId(request);
        Page<Visite> resultPage = visiteService.enCoursVisitList(orgId, PageRequest.of(page, size));//PageRequest.of(page, size)
        List<Listview> listVisit = new ArrayList<>();
        resultPage.forEach(visite ->
            listVisit.add(buildListView(visite, visiteService, gieglanFileService,catSer))
        );

        //convert list to page for applying hatoas
        log.info("------------Avant le hatoas ");
        Page<Listview> pages = new PageImpl<>(listVisit, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id")), visiteService.enCoursVisitList(orgId).size());
        PagedModel<EntityModel<Listview>> result = pagedResourcesAssembler
                .toModel(pages);
        log.info("**************après le hatoas ");
        return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "OK", result);

    }

    @GetMapping(value = "/api/v1/all/visitesended", params = { "page", "size" })
    public ResponseEntity<Object> getAllAcitveViset(@RequestParam("page") int page,
                                                    @RequestParam("size") int size) {

        Long orgId = SessionData.getOrganisationId(request);

        Page<Visite> resultPage = visiteService.endedVisitList(orgId, PageRequest.of(page, size));//PageRequest.of(page, size)
        List<Listview> listVisit = new ArrayList<>();

        log.info("Liste des visites terminées");
        resultPage.forEach(visite ->
            listVisit.add(buildListView(visite, visiteService, gieglanFileService,catSer))
        );

        //convert list to page for applying hatoas
        Page<Listview> pages = new PageImpl<>(listVisit, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id")), visiteService.endedVisitList(orgId).size());
        PagedModel<EntityModel<Listview>> result = pagedResourcesAssembler
                .toModel(pages);
        return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "OK", result);

    }

    @GetMapping("/api/v1/visite/codestatut/{status}")
    public ResponseEntity<Object> visiteByStatut(@PathVariable int status) {
        try {
            log.info("Liste des visites en cours");

            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "liste des visite en cours", visiteService.listParStatus(status));
        } catch (Exception e) {

            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "KO", null);
        }
    }

    @GetMapping("/api/v1/visites")
    public ResponseEntity<Object> visites() {
        try {
            log.info("Liste des visites");
            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "liste des visite en cours", visiteService.findAll());
        } catch (Exception e) {
            log.error("Erreur lors de l'affichage de la liste des visite en cours");
            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "Erreur lors de l'affichage"
                    + " de la liste des visite en cours", null);
        }
    }

    @GetMapping("/api/v1/visit/kanbanview")
    public ResponseEntity<Object> listforKabanView() {

                log.info("kaban view visit");
                List<KabanViewVisit> kabanViewVisits = new ArrayList<>();
                List<KanBanSimpleData> majs = visiteService.listParStatusForkanban(0);
                List<KanBanSimpleData> inspects = visiteService.listParStatusForkanban(1);
                List<KanBanSimpleData> tests = visiteService.listParStatusForkanban(2);
                List<KanBanSimpleData> toSign = visiteService.listParStatusForkanban(3);
                List<KanBanSimpleData> confirms = visiteService.listParStatusForkanban(4);
                List<KanBanSimpleData> unconforms = visiteService.listParStatusForkanban(5);
                List<KanBanSimpleData> prints = visiteService.listParStatusForkanban(6);
                List<KanBanSimpleData> refused = visiteService.listParStatusForkanban(7);
                List<KanBanSimpleData> certifies = visiteService.listParStatusForkanban(8);
                List<KanBanSimpleData> accepted = visiteService.listParStatusForkanban(9);
                List<KanBanSimpleData> approuve = visiteService.listParStatusForkanban(10);
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
                return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "Affichage Kaban view visit", kabanViewVisits);

    }

    @GetMapping("/api/v1/visite/graphview")
    public ResponseEntity<Object> listforGraphView() {
        try {
            log.info("Graphe view visit");
            List<Object> graphViews = new ArrayList<>();
            int[] datas = new int[9];
            for (int i = 0; i < datas.length; i++) {
                datas[i] = visiteService.listParStatus(i).size();
            }
            Map<String, int[]> result = new HashMap<>();
            result.put("tab", datas);
            graphViews.add(new GraphView("maj", visiteService.listParStatus(0).size()));
            graphViews.add(new GraphView("A inspecter", visiteService.listParStatus(1).size()));
            graphViews.add(new GraphView("En cours test", visiteService.listParStatus(2).size()));
            graphViews.add(new GraphView("A signer", visiteService.listParStatus(3).size()));
            graphViews.add(new GraphView("A imprimer", visiteService.listParStatus(4).size()));
            graphViews.add(new GraphView("A enregister", visiteService.listParStatus(5).size()));
            graphViews.add(new GraphView("A certifier", visiteService.listParStatus(6).size()));
            graphViews.add(new GraphView("Accepté", visiteService.listParStatus(7).size()));
            graphViews.add(new GraphView("Refusé", visiteService.listParStatus(8).size()));

            return ApiResponseHandler.generateResponses(HttpStatus.OK, true, "Affichage graph view visit", graphViews, datas);
        } catch (Exception e) {
            log.error("Erreur lors de l'affichage de la liste des visite en cours");
            return ApiResponseHandler.generateResponse(HttpStatus.OK, false, "Erreur lors de l'affichage"
                    + " de la liste des visite en cours", null);
        }

    }

    @GetMapping("/api/v1/all/visite/listview")
    public ResponseEntity<Object> listforlistView() {

        log.info("list view visit");
        List<Listview> listVisit = new ArrayList<>();
        for (Visite visite : visiteService.listParStatus(0)) {
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
            lv.setDate(visite.getDateDebut());
            lv.setReference(visite.getCarteGrise().getNumImmatriculation());
            lv.setStatut(visite.statutRender(visite.getStatut()));
            lv.setType(visite.typeRender());
            listVisit.add(lv);
            lv.setId(visite.getIdVisite());

        }
        return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "Affichage en mode liste des visites", listVisit);

    }

    @GetMapping("/api/v1/all/visite/listview/{statutCode}")
    public ResponseEntity<Object> listforlistView(@PathVariable int statutCode) {

        log.info("list view visit");
        List<Listview> listVisit = new ArrayList<>();
        visiteService.listParStatus(statutCode).forEach(
                visite -> listVisit.add(buildListView(visite, visiteService, gieglanFileService,catSer))
        );
        return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "Affichage en mode liste des visites", listVisit);

    }

    @GetMapping("/api/v1/visites/imprimer/pv/{visiteId}")
    public String printPV(@PathVariable Long visiteId) throws ImpressionException, IOException {

        log.info("Impression PV");

        File f= new File(environment.getProperty("pv.path"));
        if(!f.exists())
            f.mkdirs();

        String outputFolder = environment.getProperty("pv.path") + File.separator + visiteId.toString() + ".pdf";


        OutputStream outputStream =  new FileOutputStream(outputFolder);
        try {

            createWatermark(visiteService.findById(visiteId).getProcess().getReference());

            ITextRenderer renderer = new ITextRenderer();
            renderer.getSharedContext().setReplacedElementFactory(new MediaReplacedElementFactory(renderer.getSharedContext().getReplacedElementFactory()));
            renderer.setDocumentFromString(fillHtmlToValue(visiteId));
            renderer.layout();
            renderer.createPDF(outputStream);

        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            outputStream.close();
        }


            Visite visite = visiteService.findById(visiteId);
            if(visite.getProcess().isStatus()){
                visite.setStatut(8);
            }
            else{
                visite.setStatut(7);
                visite.setEncours(false);
            }

            visite = visiteService.add(visite);
        applicationEventPublisher.publishEvent(new VisiteCreatedEvent(visite));
        VisiteController.dispatchEdit(visite,
                visiteService, gieglanFileService, catSer);
        f = null;
            return "/public/pv/"+visiteId+".pdf";

    }

    @GetMapping("/api/v1/visites/approuver/{visiteId}")
    public ResponseEntity<Object> approuver(@PathVariable Long visiteId) {
        try {
            log.info("Demande d'approbation d'une visite...");
            Visite visite = visiteService.findById(visiteId);
            visite = visiteService.approuver(visite);
            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "Visite approuvée avec succès", visite);
        } catch (Exception e) {
            log.error("Erreur lors de l'approbation");
            return ApiResponseHandler.generateResponse(HttpStatus.OK, false, "Erreur lors de l'approbation", null);
        }
    }

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
        v.setDate(visite.getDateDebut());
        v.setReference(visite.getCarteGrise().getNumImmatriculation());
        v.setStatut(visite.statutRender(visite.getStatut()));
        v.setType(visite.typeRender());
        return v;
    }
    public String fillHtmlToValue(long id) {

        Optional<Visite> visite = this.visiteRepo.findById(id);
        Taxe tp = taxeService.findByNom("TVA");
        if (visite.isPresent()) {
            List<RapportDeVisite> rapports = this.rapportDeVisiteRepo.getRapportDeVisite(visite.get());
            this.visiteRepo.getLastVisiteWithTestIsOk(visite.get().getControl(), visite.get())
                .forEach(visite1 ->  rapports.addAll(visite1.getRapportDeVisites()));

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

            UserDTO user = UserInfoIn.getInfosControleur(visite.get().getInspection().getControleur(), environment);

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

    public void createWatermark(String ref){
        StringBuilder bld = new StringBuilder();
        for (int i = 0; i < 3500; ++i) {
            if(i % 25 == 0)
                bld.append(ref + "\n\n\n\n");
            else
                bld.append(ref + " ");
        }
        ref =  bld.toString();

        BufferedImage img = new BufferedImage(895, 1142, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = img.createGraphics();

        AffineTransform affineTransform = new AffineTransform();
        g2d.setTransform(affineTransform);
        g2d.rotate(-Math.PI/4);
        Font font = new Font("Arial", Font.PLAIN, 5);

        g2d.setColor(Color.YELLOW);


        drawParagraph(g2d, ref, 1500);
        g2d.setFont(font);

        g2d.dispose();
        try {
            ImageIO.write(img, "png", new File("watermark.png"));
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
    }

    void drawParagraph(Graphics2D g, String paragraph, float width) {
        LineBreakMeasurer linebreaker = new LineBreakMeasurer(new AttributedString(paragraph)
                .getIterator(), g.getFontRenderContext());



        int y = 0;
        while (linebreaker.getPosition() < paragraph.length()) {
            TextLayout textLayout = linebreaker.nextLayout(width);

            y += textLayout.getAscent();
            y += textLayout.getAscent();
            y += textLayout.getAscent();

            textLayout.draw(g, -785, y);
            y += textLayout.getDescent() + textLayout.getLeading();
            y += textLayout.getDescent() + textLayout.getLeading();
            y += textLayout.getDescent() + textLayout.getLeading();
        }
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
            //Openaplr to know if the car is in the center

            String uri = environment.getProperty("endpoint.openalpr") ;
            String apiKey = environment.getProperty("endpoint.openalpr.api.key") ;
            Visite visite = visiteService.findById(id);
            visite.getInspection().setDistancePercentage(
                    openAlprService.getPresenceConfidence(uri,apiKey,visite.getInspection())
            );
            visiteService.add(visite);


            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "OK", response);



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
        visiteService.findActiveVisites().forEach( visite ->
            listVisit.add(buildListView(visite, visiteService, gieglanFileService,catSer))
        );
        return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "OK", listVisit);
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

    @PostMapping(value = "/api/v1/admin/visites/reset/{id}")
    public ResponseEntity<Object> reset(@PathVariable Long id) {

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
        v.getInspection().setVisiteIdReseted(v.getIdVisite());

        v = visiteService.add(v);

        return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "OK", v);
    }

    @GetMapping(value = "/api/v1/admin/visites/{id}")
    public ResponseEntity<Object> getOneVisite(@PathVariable Long id) {

        Visite visite = visiteService.findById(id);

        return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "OK", visite);

    }

    @DeleteMapping(value = "/api/v1/admin/visites/{id}")
    public ResponseEntity<Object> delVisite(@PathVariable Long id) {

        Visite visite = visiteService.findById(id);
        visite.setStatut(0);
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
                Objects.equals(applicationEventPublisher, that.applicationEventPublisher) &&
                Objects.equals(pagedResourcesAssembler, that.pagedResourcesAssembler);
    }

    @Override
    public int hashCode() {

        return Objects.hash(environment, request, visiteRepo, openAlprService, messageRepository, rapportListService, pdfGenaratorUtil, visiteService, rapportDeVisiteRepo, mesureVisuelRepository, venteService, taxeService, storageService, ps, gieglanFileService, catSer, applicationEventPublisher, pagedResourcesAssembler);
    }
}
