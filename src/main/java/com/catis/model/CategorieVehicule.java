package com.catis.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.model.configuration.JournalData;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class CategorieVehicule extends JournalData {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String type;

	@OneToMany(mappedBy = "categorieVehicule")
	private Set<CategorieVehiculeProduit> categorieVehiculeProduits;

	@OneToMany(mappedBy = "categorieVehicule")
	private Set<CategorieTestVehicule> categorieTestVehicules;

	public CategorieVehicule() {
		// TODO Auto-generated constructor stub
	}

	public CategorieVehicule(Long id, String type, Set<CategorieVehiculeProduit> categorieVehiculeProduits,
			Set<CategorieTestVehicule> categorieTestVehicules) {
		this.id = id;
		this.type = type;
		this.categorieVehiculeProduits = categorieVehiculeProduits;
		this.categorieTestVehicules = categorieTestVehicules;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Set<CategorieVehiculeProduit> getCategorieVehiculeProduits() {
		return categorieVehiculeProduits;
	}

	public void setCategorieVehiculeProduits(Set<CategorieVehiculeProduit> categorieVehiculeProduits) {
		this.categorieVehiculeProduits = categorieVehiculeProduits;
	}

	public Set<CategorieTestVehicule> getCategorieTestVehicules() {
		return categorieTestVehicules;
	}

	public void setCategorieTestVehicules(Set<CategorieTestVehicule> categorieTestVehicules) {
		this.categorieTestVehicules = categorieTestVehicules;
	}

}