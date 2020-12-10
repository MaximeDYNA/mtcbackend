package com.catis.Controller.pdfhandler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;

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
import com.catis.model.Visite;
import com.catis.objectTemporaire.Listview;
import com.catis.service.InspectionService;
import com.catis.service.PdfService;
import com.catis.service.VisiteService;
import com.google.zxing.WriterException;
import com.lowagie.text.DocumentException;

@RestController
public class pdfController {
	
		
	 	private VisiteService visiteService;
	 	
	    private PdfService pdfService;
	    private InspectionService inspectionService;
	    
	    @Autowired
	    public pdfController(VisiteService visiteService, PdfService pdfService, InspectionService inspection) {
			super();
			this.visiteService = visiteService;
			this.pdfService = pdfService;
			this.inspectionService = inspection;
		}
		@GetMapping("/visites/{id}")
	    public ModelAndView studentsView(ModelAndView modelAndView, @PathVariable long id) throws WriterException, IOException {
	    	
	    		Visite v = visiteService.findById(id);
	    		Inspection i = inspectionService.findInspectionByVisite(v.getIdVisite()); 
		    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/YY HH:mm");
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
