package com.catis.Controller.pdfhandler;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.catis.model.*;
import com.catis.objectTemporaire.*;
import com.lowagie.text.pdf.codec.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import static com.itextpdf.text.pdf.BaseFont.EMBEDDED;
import static com.itextpdf.text.pdf.BaseFont.IDENTITY_H;

import com.catis.Controller.configuration.CryptoUtil;
import com.catis.Controller.configuration.QRCodeGenerator;
import com.catis.repository.RapportDeVisiteRepo;
import com.catis.repository.VisiteRepository;
import com.catis.service.InspectionService;
import com.catis.service.PdfService;
import com.catis.service.TaxeService;
import com.catis.service.VenteService;
import com.catis.service.VisiteService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.lowagie.text.DocumentException;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.w3c.tidy.Tidy;
import org.xhtmlrenderer.pdf.ITextRenderer;


@RestController
public class pdfController {

    @Autowired
    HttpServletRequest request;
    @Autowired
    private VisiteRepository visiteRepo;
    @Autowired
    private Environment environment;

    @Autowired
    PdfGenaratorUtil pdfGenaratorUtil;

    private VisiteService visiteService;

    @Autowired
    private RapportDeVisiteRepo rapportDeVisiteRepo;

    private PdfService pdfService;

    private InspectionService inspectionService;

    private VenteService venteService;

    private TaxeService taxeService;

    @Autowired
    public pdfController(VisiteService visiteService, PdfService pdfService,
                         InspectionService inspection, VenteService venteService, TaxeService taxProduitService) {
        super();
        this.visiteService = visiteService;
        this.pdfService = pdfService;
        this.inspectionService = inspection;
        this.venteService = venteService;
        this.taxeService = taxProduitService;
    }

    @GetMapping("/visites/{id}/verso")
    public ModelAndView versoPV(ModelAndView modelAndView, @PathVariable long id) throws WriterException, IOException {
        Optional<Visite> visite = this.visiteRepo.findById(id);
        modelAndView.addObject("visite", visite.get());
        modelAndView.setViewName("pvVerso");

        return modelAndView;
    }



    public String parseThymeleafTemplate(long id) throws Exception {

        Optional<Visite> visite = this.visiteRepo.findById(id);
        Taxe tp = taxeService.findByNom("TVA");
        if (visite.isPresent()) {
            List<RapportDeVisite> rapports = this.rapportDeVisiteRepo.getRapportDeVisite(visite.get());
            List<Visite> lastVisiteWithTestIsOk = this.visiteRepo.getLastVisiteWithTestIsOk(visite.get().getControl(), visite.get());
            lastVisiteWithTestIsOk.forEach(visite1 -> {
                rapports.addAll(visite1.getRapportDeVisites());
            });
            HashMap<String, String> results = new HashMap<>();
            List<Lexique> defaultsTest = new ArrayList<>();
            rapports.forEach(rapport -> {
                results.put(rapport.getSeuil().getFormule().getMesures().stream().findFirst().get().getCode(), rapport.getResult());
                if (rapport.getSeuil().getLexique() != null) {
                    defaultsTest.add(rapport.getSeuil().getLexique());
                }
            });

            UserDTO user = UserInfoIn.getInfosControleur(visite.get().getInspection().getControleur(), request,
                    environment.getProperty("keycloak.auth-server-url"), environment.getProperty("keycloak.realm"));

            ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
            templateResolver.setSuffix(".html");
            templateResolver.setTemplateMode(TemplateMode.HTML);

            TemplateEngine templateEngine = new TemplateEngine();
            templateEngine.setTemplateResolver(templateResolver);

            Context context = new Context();
            context.setVariable("v", visite.get());
            context.setVariable("tp", tp);
            context.setVariable("result", results);
            context.setVariable("defaultsTest", defaultsTest);
            context.setVariable("controlleurName", user.getNom() + " " + user.getPrenom());

            /*modelAndView.addObject("v", visite.get());
            modelAndView.addObject("tp", tp);
            modelAndView.addObject("result", results);
            modelAndView.addObject("defaultsTest", defaultsTest);
            modelAndView.addObject("controlleurName", user.getNom() + " " + user.getPrenom());
            modelAndView.setViewName("visites");*/


            return templateEngine.process("templates/visites", context);
        }

        return null;

    }
    @GetMapping("/api/v1/visites/imprimer/{id}")
    public void generatePdfFromHtml(@PathVariable Long id) throws Exception {
        String outputFolder = "C:/PV/" + File.separator + id.toString() + ".pdf";
        OutputStream outputStream = new FileOutputStream(outputFolder);

        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(
                parseThymeleafTemplate(id)
        );
        renderer.layout();
        renderer.createPDF(outputStream);

        outputStream.close();

    }

    public void generatePdfFromxHtml(@PathVariable Long id) throws Exception {
        String outputFolder = "C:/PV/" + File.separator + id.toString() + ".pdf";

        String xHtml = convertToXhtml(parseThymeleafTemplate(id));

        ITextRenderer renderer = new ITextRenderer();
        //renderer.getFontResolver().addFont("Code39.ttf", IDENTITY_H, EMBEDDED);

        // FlyingSaucer has a working directory. If you run this test, the working directory
        // will be the root folder of your project. However, all files (HTML, CSS, etc.) are
        // located under "/src/test/resources". So we want to use this folder as the working
        // directory.

        String baseUrl = FileSystems
                .getDefault()
                .getPath("src","main","resources", "templates")
                .toUri()
                .toURL()
                .toString();
        renderer.setDocumentFromString(xHtml, baseUrl);
        renderer.layout();

        // And finally, we create the PDF:
        OutputStream outputStream = new FileOutputStream(outputFolder);
        renderer.createPDF(outputStream);
        outputStream.close();



        outputStream.close();
    }

    @GetMapping("/visites/qrcode/{id}")
    public ResponseEntity<byte[]> qr(@PathVariable final Long id) throws WriterException, IOException {
        Visite v = visiteService.findById(id);
        System.out.println("qrcode en cours de fabrication...");


        byte[] bytes = QRCodeGenerator.getQRCodeImage(CryptoUtil.encrypt(v.getIdVisite().toString(), "password"), 50, 50);

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);

        return new ResponseEntity<byte[]>(bytes, headers, HttpStatus.CREATED);

    }

    @GetMapping("/download-pdf")
    public void downloadPDFResource(HttpServletResponse response) {
        try {
            Path file = Paths.get(pdfService.generatePdf().getAbsolutePath());
            if (Files.exists(file)) {
                response.setContentType("application/pdf");
                response.addHeader("Content-Disposition",
                        "attachment; filename=" + file.getFileName());
                Files.copy(file, response.getOutputStream());
                response.getOutputStream().flush();
            }
        } catch (DocumentException | IOException ex) {
            ex.printStackTrace();
        }
    }

    @GetMapping(value = "/qrcode")
    public ResponseEntity<byte[]> generateQRCode() throws Exception {
        byte[] bytes = QRCodeGenerator.getQRCodeImage("Noms & Prénoms : DYNA NGOTHY Maxime Jacques\r\n"
                + "Fonction :  Ingénieur - Service Recherche et Developpement\r\n"
                + "CNI No : 000771075\r\n"
                + "Matricule : P-C010\r\n"
                + "Contacts : +237 690 981 943 / 675 807 434\r\n"
                + "email: m.dyna@prooftagcatis.com", 200, 200);
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);

        return new ResponseEntity<byte[]>(bytes, headers, HttpStatus.CREATED);
    }
    private String convertToXhtml(String html) throws UnsupportedEncodingException {
        Tidy tidy = new Tidy();
        tidy.setInputEncoding("UTF-8");
        tidy.setOutputEncoding("UTF-8");
        tidy.setXHTML(true);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes("UTF-8"));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        tidy.parseDOM(inputStream, outputStream);
        return outputStream.toString("UTF-8");
    }


}
