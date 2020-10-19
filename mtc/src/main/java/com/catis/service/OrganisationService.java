package com.catis.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.catis.model.Organisation;
import com.catis.repository.OrganisationRepository;

@Service
public class OrganisationService {

	@Autowired
	private OrganisationRepository organisationRepository;
	
	public List<Organisation> findAllOrganisation(){
		List<Organisation> organisations = new ArrayList<>();
		organisationRepository.findAll().forEach(organisations::add);
		return organisations;
	}
}
