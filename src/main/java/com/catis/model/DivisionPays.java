package com.catis.model;



import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.model.configuration.JournalData;




@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "t_divisionpays")
public class DivisionPays extends JournalData{
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long divisionPaysId;
	
	@ManyToOne
	private Pays pays;
	@OneToMany(mappedBy = "divisionPays")
	Set<Adresse> adresses;
	private String libelle;
	private String description;
	
	
	private String idDivisionPaysParent;

	public DivisionPays() {
		
	}


	public DivisionPays(Long divisionPaysId, Pays pays, String libelle, String description,
			String idDivisionPaysParent) {
		super();
		this.divisionPaysId = divisionPaysId;
		this.pays = pays;
		this.libelle = libelle;
		this.description = description;
		this.idDivisionPaysParent = idDivisionPaysParent;
	}

	public Long getDivisionPaysId() {
		return divisionPaysId;
	}

	public void setDivisionPaysId(Long divisionPaysId) {
		this.divisionPaysId = divisionPaysId;
	}

	public Pays getPays() {
		return pays;
	}


	public void setPays(Pays pays) {
		this.pays = pays;
	}

	public String getIdDivisionPaysParent() {
		return idDivisionPaysParent;
	}



	public void setIdDivisionPaysParent(String idDivisionPaysParent) {
		this.idDivisionPaysParent = idDivisionPaysParent;
	}



	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	public Set<Adresse> getAdresses() {
		return adresses;
	}


	public void setAdresses(Set<Adresse> adresses) {
		this.adresses = adresses;
	}


}
