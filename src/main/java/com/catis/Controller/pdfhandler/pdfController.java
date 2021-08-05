package com.catis.Controller.pdfhandler;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.catis.model.entity.Visite;
import com.catis.repository.MesureVisuelRepository;
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

import com.catis.Controller.configuration.CryptoUtil;
import com.catis.Controller.configuration.QRCodeGenerator;
import com.catis.repository.RapportDeVisiteRepo;
import com.catis.repository.VisiteRepository;
import com.catis.service.InspectionService;
import com.catis.service.PdfService;
import com.catis.service.TaxeService;
import com.catis.service.VenteService;
import com.catis.service.VisiteService;
import com.google.zxing.WriterException;
import com.lowagie.text.DocumentException;
import org.w3c.tidy.Tidy;


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

    @Autowired
    private MesureVisuelRepository mesureVisuelRepository;

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

    @GetMapping("/api/v1/visites/imprimer/{id}")
    public void generatePdfFromHtml(@PathVariable Long id) throws Exception { }

    @GetMapping("/public/visites/qrcode/{id}")
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

    public static Date convert(LocalDateTime dateToConvert) {
        return Date.from(dateToConvert
            .atZone(ZoneId.systemDefault())
            .toInstant()
        );
    }

}
