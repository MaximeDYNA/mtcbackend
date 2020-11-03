package com.catis.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.catis.model.Organisation;
import com.catis.service.OrganisationService;

@RestController
public class OrganisationController {

	@Autowired
	private OrganisationService organisationService;
	
	@RequestMapping(method = RequestMethod.POST, value="/api/v1/organisations")
	public void creerOrganisation(@RequestBody Organisation organisation) {
		organisationService.addOrgansiation(organisation);
	}
}
