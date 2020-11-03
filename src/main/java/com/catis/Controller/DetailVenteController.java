package com.catis.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.catis.model.DetailVente;
import com.catis.service.DetailVenteService;

@RestController
public class DetailVenteController {

	@Autowired
	private DetailVenteService detailVenteService;
	
	@RequestMapping(method = RequestMethod.POST, value="/api/v1/detailsventes")
	public void addVente(List<DetailVente> detailVentes) {
		detailVenteService.addVentes(detailVentes);
	}
	
	
}
