package com.catis.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.catis.model.CaissierCaisse;
import com.catis.service.CaissierCaisseService;

@RestController
public class CaissierCaisseController {
	
	@Autowired
	private CaissierCaisseService caissierCaisseService;
	
	@RequestMapping("/caissiercaisses")
	public List<CaissierCaisse> afficherlescaissesetcaissiers(){
		return caissierCaisseService.findAllCaissierCaisse();
	}
	@RequestMapping(method = RequestMethod.POST, value="/caissiercaisses")
	public void creercaissierCaisse(@RequestBody CaissierCaisse caissiercaisse){
		caissierCaisseService.addCaissierCaisse(caissiercaisse);
	}
	@RequestMapping(method = RequestMethod.DELETE, value="/caissiercaisses/{id}")
	public void supprimercaissierCaisse(@RequestBody @PathVariable String id){
		caissierCaisseService.deleteCaissierCaisseById(id);
	}
	@RequestMapping(method = RequestMethod.GET, value="/caissiercaisses/{id}")
	public CaissierCaisse trouverCaissierCaisse(@RequestBody @PathVariable String id){
		return caissierCaisseService.findCaisseById(id);
	}
}
