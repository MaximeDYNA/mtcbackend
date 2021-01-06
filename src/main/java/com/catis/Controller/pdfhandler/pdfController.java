package com.catis.Controller.pdfhandler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.catis.Controller.configuration.QRCodeGenerator;
import com.catis.model.Inspection;
import com.catis.model.Taxe;
import com.catis.model.Visite;
import com.catis.objectTemporaire.CategorieTests;
import com.catis.objectTemporaire.Listview;
import com.catis.objectTemporaire.TestList;
import com.catis.service.InspectionService;
import com.catis.service.PdfService;
import com.catis.service.TaxeService;
import com.catis.service.VenteService;
import com.catis.service.VisiteService;
import com.google.zxing.WriterException;
import com.lowagie.text.DocumentException;

@RestController
public class pdfController {
	
		
	 	private VisiteService visiteService;
	 	
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
	    	
	    		        
		        modelAndView.setViewName("pvVerso");		        
		        return modelAndView;
	
	    }
		@GetMapping("/visites/{id}")
	    public ModelAndView studentsView(ModelAndView modelAndView, @PathVariable long id) throws WriterException, IOException {
	    	
	    		Visite v = visiteService.findById(id);
	    		Inspection i = inspectionService.findInspectionByVisite(v.getIdVisite());
	    		Taxe tp = taxeService.findByNom("TVA");
		    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/YY HH:mm");
		    	DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("YY");
		    	DateTimeFormatter monthFomatter = DateTimeFormatter.ofPattern("MM");
		    	String pattern = "dd/MM/YY";
		    	SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
				String type;
				
				if(v.isContreVisite())
					type = "Contre visite";
				else
					type = "Contrôle initial";
				
		    	Listview dateparser = new Listview();
		    	dateparser.setDate(v.getDateDebut());
		        modelAndView.addObject("id", v.getIdVisite());
		        modelAndView.addObject("date", v.getDateDebut().format(formatter) );
		        modelAndView.addObject("type", type );
		        modelAndView.addObject("cat", v.getCarteGrise().getProduit().getLibelle() );
		        modelAndView.addObject("imm", v.getCarteGrise().getNumImmatriculation() );
		        modelAndView.addObject("chassis", v.getCarteGrise().getVehicule().getChassis() );
		        modelAndView.addObject("marq", v.getCarteGrise().getVehicule().getMarqueVehicule().getLibelle() );
		        modelAndView.addObject("energie", v.getCarteGrise().getVehicule().getEnergie().getLibelle() );
		        modelAndView.addObject("dmc", simpleDateFormat.format(v.getCarteGrise().getVehicule().getPremiereMiseEnCirculation()) );
		        modelAndView.addObject("kil", i.getKilometrage() );
		        modelAndView.addObject("aff", v.getCarteGrise().getCentre_ssdt() );
		        modelAndView.addObject("owner", v.getCarteGrise().getProprietaireVehicule().getPartenaire().getNom() +" "+ 
		        								v.getCarteGrise().getProprietaireVehicule().getPartenaire().getPrenom());
		        modelAndView.addObject("adresse", v.getCarteGrise().getCommune()); 
		        modelAndView.addObject("tel", v.getCarteGrise().getProprietaireVehicule().getPartenaire().getTelephone()); 
		        modelAndView.addObject("prixHt", v.getCarteGrise().getProduit().getPrix()==0?0:v.getCarteGrise().getProduit().getPrix()); 		
		        modelAndView.addObject("taxe", tp.getValeur()==0?0:tp.getValeur());	        
		        LocalDateTime now = LocalDateTime.now(); 
		        
		        modelAndView.addObject("day", now.format(monthFomatter));
		        modelAndView.addObject("year", now.format(formatter2));
		        
		        List<TestList> testlist =  new ArrayList() {{
		            add(new TestList("Eff AG \n LF Eff","10%"));
		            add(new TestList("Eff AD","20%"));
		            add(new TestList("Eff RG","20%"));
		            add(new TestList("Diss. AV","20%"));
		            add(new TestList("Diss. AR","20%"));
		            add(new TestList("Diss. AR","20%"));
		        }};
		        List<TestList> testlist2 =  new ArrayList() {{
		            add(new TestList("eff AG","20%"));
		            add(new TestList("eff AG","20%"));
		           
		        }};
		        List<TestList> testlistRipage =  new ArrayList() {{
		            add(new TestList("ripage av", "20%"));
		            add(new TestList("ripage arr","20%"));
		           
		        }};
		        CategorieTests ct = new CategorieTests( "SUSPENSION", testlist);
		        CategorieTests cr = new CategorieTests( "RIPAGE / SHIFT.", testlistRipage);
		        CategorieTests c = new CategorieTests( "FREINS / BRAKES", testlist2);
		        CategorieTests pol = new CategorieTests( "POLLUTION", testlist2);
		        CategorieTests phares = new CategorieTests( "PHARES / LAMPS", testlist2);
		        List<CategorieTests> cts = new ArrayList<>();
		        cts.add(ct);
		        cts.add(cr);
		        cts.add(pol);
		        cts.add(phares);
		        cts.add(c);

		        modelAndView.addObject("categorieTests", cts );		        
		        modelAndView.setViewName("visites");		        
		        return modelAndView;
	
	    }
	    @GetMapping ("/visites/qrcode/{id}")
	    public ResponseEntity<byte[]> qr(@PathVariable final Long id) throws WriterException, IOException {
	    	Visite v = visiteService.findById(id);
	    	System.out.println("qrcode en cours de fabrication...");
	    	

	        byte[] bytes = QRCodeGenerator.getQRCodeImage(v.getIdVisite().toString(), 60, 60);

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
}
