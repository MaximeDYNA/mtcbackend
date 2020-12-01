package com.catis.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.catis.model.Client;
import com.catis.model.Contact;
import com.catis.model.Partenaire;
import com.catis.model.ProprietaireVehicule;
import com.catis.repository.ProprietaireVehiculeRepository;

@Service
public class ProprietaireVehiculeService {

	@Autowired
	private ProprietaireVehiculeRepository pvr;
	@Autowired
	private PartenaireService partenaireService;
	

	public List<ProprietaireVehicule> findAll(){
		List<ProprietaireVehicule> proprietaires = new ArrayList<>();
		pvr.findAll().forEach(proprietaires::add);
		return proprietaires;
	}
	public ProprietaireVehicule addProprietaire(ProprietaireVehicule p){
		return pvr.save(p);
	}
	public ProprietaireVehicule findById(Long id){
		return pvr.findById(id).get();
	}
	public ProprietaireVehicule addClientToProprietaire(Client client){
		Partenaire p = partenaireService.findPartenaireById(client.getPartenaire().getPartenaireId());
		if(pvr.findByPartenaire_PartenaireId(p.getPartenaireId()).isEmpty()) {
			ProprietaireVehicule pro = new ProprietaireVehicule();
			pro.setPartenaire(p);
			return pvr.save(pro);
		}
		return pvr.findByPartenaire_PartenaireId(p.getPartenaireId()).get(0);
	
	}
	
	public ProprietaireVehicule addContactToProprietaire(Contact contact){
		Partenaire p = partenaireService.findPartenaireById(contact.getPartenaire().getPartenaireId());
		if(pvr.findByPartenaire_PartenaireId(p.getPartenaireId()).isEmpty()) {
			ProprietaireVehicule pro = new ProprietaireVehicule();
			pro.setPartenaire(p);
			return pvr.save(pro);
		}
		return pvr.findByPartenaire_PartenaireId(p.getPartenaireId()).get(0);
	
	}
}
