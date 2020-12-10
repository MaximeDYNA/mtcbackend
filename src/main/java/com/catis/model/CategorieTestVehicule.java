package com.catis.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class CategorieTestVehicule {

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	private CategorieTest categorieTest;

	@ManyToOne
	private CategorieVehicule categorieVehicule;

	@OneToMany(fetch = FetchType.LAZY, mappedBy="categorieTestVehicule")
	private Set<Mesure> mesures;
	
	@OneToMany(mappedBy = "categorieTestVehicule")
	private Set<GieglanFile> gieglanFiles;

	public CategorieTestVehicule() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CategorieTestVehicule(Long id, CategorieTest categorieTest, CategorieVehicule categorieVehicule,
			Set<Mesure> mesures, Set<GieglanFile> gieglanFiles) {
		super();
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
	
	
}