package com.catis.Controller.pdfhandler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.lowagie.text.DocumentException;


@RestController
public class pdfController {	
	
	@Autowired
	HttpServletRequest request;
	@Autowired
	private VisiteRepository visiteRepo;
	@Autowired
	private Environment environment;
	
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

	@GetMapping("/api/v1/visites/imprimer/{id}")
	public ModelAndView showProcessVerval(ModelAndView modelAndView, @PathVariable long id) {

		Optional<Visite> visite = this.visiteRepo.findById(id);
		Taxe tp = taxeService.findByNom("TVA");
		if (visite.isPresent()) {
			List<RapportDeVisite> rapports = this.rapportDeVisiteRepo.getRapportDeVisite(visite.get());
			List<Visite> lastVisiteWithTestIsOk = this.visiteRepo.getLastVisiteWithTestIsOk(visite.get().getControl(), visite.get());
			lastVisiteWithTestIsOk.forEach(visite1 -> { rapports.addAll(visite1.getRapportDeVisites());});
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
			modelAndView.addObject("v", visite.get());
			modelAndView.addObject("tp", tp);
			modelAndView.addObject("result", results);
			modelAndView.addObject("defaultsTest", defaultsTest);
			modelAndView.addObject("controlleurName", user.getNom()+" "+user.getPrenom());
			modelAndView.setViewName("visites");
			return modelAndView;
		}

		return modelAndView;
	}

	@GetMapping ("/visites/qrcode/{id}")
	public ResponseEntity<byte[]> qr(@PathVariable final Long id) throws WriterException, IOException {
		Visite v = visiteService.findById(id);
		System.out.println("qrcode en cours de fabrication...");


		byte[] bytes = QRCodeGenerator.getQRCodeImage(CryptoUtil.encrypt(v.getIdVisite().toString(), "password"), 70, 70);

		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_PNG);

		return new ResponseEntity<byte[]> (bytes, headers, HttpStatus.CREATED);

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
		byte[] bytes  = QRCodeGenerator.getQRCodeImage("Noms & Prénoms : DYNA NGOTHY Maxime Jacques\r\n"
				+ "Fonction :  Ingénieur - Service Recherche et Developpement\r\n"
				+ "CNI No : 000771075\r\n"
				+ "Matricule : P-C010\r\n"
				+ "Contacts : +237 690 981 943 / 675 807 434\r\n"
				+ "email: m.dyna@prooftagcatis.com", 200, 200);
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_PNG);

		 return new ResponseEntity<byte[]> (bytes, headers, HttpStatus.CREATED);
	}
}
