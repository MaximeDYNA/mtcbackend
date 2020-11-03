package com.catis.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.catis.model.Caisse;
import com.catis.service.CaisseService;

@RestController
public class CaisseController {
	@Autowired
	private CaisseService caisserservice;
	
	@RequestMapping("/caisses")
	public List<Caisse> afficherLesCaisses(){
		return caisserservice.findAllCaisse();
	}
	@RequestMapping(method = RequestMethod.POST, value="/caisses")
	public void creerUneCaisse(@RequestBody Caisse caisse){
		caisserservice.addCaisse(caisse);
	}
	
}
