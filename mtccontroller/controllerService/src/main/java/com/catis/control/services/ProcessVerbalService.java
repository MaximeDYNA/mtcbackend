package com.catis.control.services;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.catis.control.dao.ProcessVerbalDao;
import com.catis.control.entities.RapportDeVisite;
import com.catis.control.entities.VerbalProcess;
import com.catis.control.entities.Visite;

import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@Transactional
public class ProcessVerbalService {

	@Autowired
	private ProcessVerbalDao processVerbalDao;

	private static Logger log = LoggerFactory.getLogger(ProcessVerbalService.class);

	private Visite visite;

	public void save(Visite visite, Set<RapportDeVisite> rapportDeVisites, Boolean status) {
		log.info("save method called for process verbal");
		this.visite = visite;
		VerbalProcess processV = new VerbalProcess();
		if(visite.isContreVisite()){
			log.info("about to associate process verbal to control visite");
		}else{
			log.info("about to associate process verbal to control initial");
		}
		processV.setVisite(visite);
		log.info("associated  process verbal to viiste complete");
		processV.setReference(this.getReferencePv());
		processV.setOrganisation(visite.getOrganisation());
		processV.setStatus(status);
		rapportDeVisites.forEach( rdv -> rdv.setVerbalProcess(processV));
		if(rapportDeVisites.isEmpty()){
			log.info("no rapport de visite found, process verbal rapport de visite will be null");
		}
		processV.setRapportDeVisites(rapportDeVisites);
		log.info("associated  process verbal to ]rapport de visite");
		this.processVerbalDao.save(processV);
		log.info("successfully saved process verbal for visite");
	}
	
	private String getReferencePv() {

		String ref = this.updateOrganisationId();
		LocalDateTime date = LocalDateTime.now();
		DateTimeFormatter myformat = DateTimeFormatter.ofPattern("yyMMddHHmmss");
		ref += date.format(myformat);

		return ref;
	}

	private String updateOrganisationId() {
		log.info("updating visite organisation");
		String id = this.visite.getOrganisation().getOrganisationId().toString();
		log.info("visite organisation id : " + id);
		while (id.length() <3) {
			id = "0"+id;
		}

		return id+"-";
	}
}
