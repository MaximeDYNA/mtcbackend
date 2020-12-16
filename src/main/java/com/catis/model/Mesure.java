package com.catis.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.model.configuration.JournalData;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "t_mesure")
@EntityListeners(AuditingEntityListener.class)
public class Mesure extends JournalData {

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long idMesure;
	private String code;
	private String description;
	
	@ManyToOne
	private Organisation organisation;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="mesure")
	@JsonIgnore
	private Set<ValeurTest> valeurtests;

	@ManyToOne
	private Formule formule;
	
	@ManyToOne
	private CategorieTestVehicule categorieTestVehicule;

	
	
	public Mesure() {
	
		// TODO Auto-generated constructor stub
	}



	



	public Mesure(Long idMesure, String code, String description, Organisation organisation, Set<ValeurTest> valeurtests,
			Formule formule, CategorieTestVehicule categorieTestVehicule) {
		
		this.idMesure = idMesure;
		this.code = code;
		this.description = description;
		this.organisation = organisation;
		this.valeurtests = valeurtests;
		this.formule = formule;
		this.categorieTestVehicule = categorieTestVehicule;
	}







	public Long getIdMesure() {
		return idMesure;
	}



	public void setIdMesure(Long idMesure) {
		this.idMesure = idMesure;
	}



	public String getCode() {
		return code;
	}



	public void setCode(String code) {
		this.code = code;
	}



	public String getDescription() {
		return description;
	}



	public void setDescription(String description) {
		this.description = description;
	}






	public Organisation getOganisation() {
		return organisation;
	}







	public void setOganisation(Organisation organisation) {
		this.organisation = organisation;
	}







	public Set<ValeurTest> getValeurtests() {
		return valeurtests;
	}



	public void setValeurtests(Set<ValeurTest> valeurtests) {
		this.valeurtests = valeurtests;
	}



	public Formule getFormule() {
		return formule;
	}



	public void setFormule(Formule formule) {
		this.formule = formule;
	}



	public CategorieTestVehicule getCategorieTestVehicule() {
		return categorieTestVehicule;
	}



	public void setCategorieTestVehicule(CategorieTestVehicule categorieTestVehicule) {
		this.categorieTestVehicule = categorieTestVehicule;
	}
	
}
