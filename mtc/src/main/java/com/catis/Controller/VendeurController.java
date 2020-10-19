package com.catis.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.catis.model.Vendeur;
import com.catis.service.VendeurService;

@RestController
public class VendeurController {

	@Autowired
	private VendeurService vendeurService;
	
	@RequestMapping("/api/v1/vendeurs")
	public List<Vendeur> afficherLesVendeurs(){
		return vendeurService.findAllVendeur();
	}
	@RequestMapping(method = RequestMethod.POST, value="/api/v1/vendeurs")
	public void creerUnVendeur(@RequestBody Vendeur vendeur){
		vendeurService.addVendeur(vendeur);
	}
}
