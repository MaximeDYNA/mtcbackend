package com.catis.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.model.configuration.JournalData;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "t_categorietest")
public class CategorieTest extends JournalData {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idCategorieTest;

	private String libelle;

	private String description;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "categorieTest")
	@JsonIgnore
	private Set<CategorieTestMachine> categorieTestMachines;

	@OneToMany(mappedBy = "categorieTest")
	private Set<CategorieTestVehicule> categorieTestVehicules;
	
	@OneToMany(mappedBy = "categorieTest")
	private Set<GieglanFile> gieglanFiles;

	public Long getIdCategorieTest() {
		return idCategorieTest;
	}

	public void setIdCategorieTest(Long idCategorieTest) {
		this.idCategorieTest = idCategorieTest;
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

	public Set<CategorieTestMachine> getCategorieTestMachines() {
		return categorieTestMachines;
	}

	public void setCategorieTestMachines(Set<CategorieTestMachine> categorieTestMachines) {
		this.categorieTestMachines = categorieTestMachines;
	}

	public Set<CategorieTestVehicule> getCategorieTestVehicules() {
		return categorieTestVehicules;
	}

	public void setCategorieTestVehicules(Set<CategorieTestVehicule> categorieTestVehicules) {
		this.categorieTestVehicules = categorieTestVehicules;
	}



	public Set<GieglanFile> getGieglanFiles() {
		return gieglanFiles;
	}

	public void setGieglanFiles(Set<GieglanFile> gieglanFiles) {
		this.gieglanFiles = gieglanFiles;
	}

	public CategorieTest(Long idCategorieTest, String libelle, String description,
			Set<CategorieTestMachine> categorieTestMachines, Set<CategorieTestVehicule> categorieTestVehicules,
			Set<GieglanFile> gieglanFiles) {
		super();
		this.idCategorieTest = idCategorieTest;
		this.libelle = libelle;
		this.description = description;
		this.categorieTestMachines = categorieTestMachines;
		this.categorieTestVehicules = categorieTestVehicules;
		this.gieglanFiles = gieglanFiles;
	}

	public CategorieTest() {
		// TODO Auto-generated constructor stub
	}

}
