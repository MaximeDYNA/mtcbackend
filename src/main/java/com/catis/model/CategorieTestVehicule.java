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

@Entity
@EntityListeners(AuditingEntityListener.class)
public class CategorieTestVehicule extends JournalData {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String type;

	@ManyToOne
	private CategorieTest categorieTest;

	@ManyToOne
	private CategorieVehicule categorieVehicule;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "categorieTestVehicule")
	private Set<Mesure> mesures;

	@OneToMany(mappedBy = "categorieTestVehicule")
	private Set<GieglanFile> gieglanFiles;

	public CategorieTestVehicule() {
		// TODO Auto-generated constructor stub
	}

	public CategorieTestVehicule(Long id, CategorieTest categorieTest, CategorieVehicule categorieVehicule,
			Set<Mesure> mesures, Set<GieglanFile> gieglanFiles) {
		this.id = id;
		this.categorieTest = categorieTest;
		this.categorieVehicule = categorieVehicule;
		this.mesures = mesures;
		this.gieglanFiles = gieglanFiles;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public CategorieTest getCategorieTest() {
		return categorieTest;
	}

	public void setCategorieTest(CategorieTest categorieTest) {
		this.categorieTest = categorieTest;
	}

	public CategorieVehicule getCategorieVehicule() {
		return categorieVehicule;
	}

	public void setCategorieVehicule(CategorieVehicule categorieVehicule) {
		this.categorieVehicule = categorieVehicule;
	}

	public Set<Mesure> getMesures() {
		return mesures;
	}

	public void setMesures(Set<Mesure> mesures) {
		this.mesures = mesures;
	}

	public Set<GieglanFile> getGieglanFiles() {
		return gieglanFiles;
	}

	public void setGieglanFiles(Set<GieglanFile> gieglanFiles) {
		this.gieglanFiles = gieglanFiles;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	

}