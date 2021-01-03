package com.catis.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.model.configuration.JournalData;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Lexique extends JournalData {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String libelle;

	private String code;

	private Boolean Visuel;
	
	@ManyToOne
	private Client client;
	
	@ManyToOne
	private CategorieVehicule categorieVehicule;

	@ManyToOne
	private VersionLexique versionLexique;

	@OneToMany(mappedBy = "parent")
	@JsonIgnore
	private Set<Lexique> childs;

	@ManyToOne
	private Lexique parent;

	private boolean haschild;
	
	public Lexique() {

	}


	public Lexique(Long id, String libelle, String code, Boolean visuel, Client client,
			CategorieVehicule categorieVehicule, VersionLexique versionLexique, Set<Lexique> childs, Lexique parent) {
		super();
		this.id = id;
		this.libelle = libelle;
		this.code = code;
		Visuel = visuel;
		this.client = client;
		this.categorieVehicule = categorieVehicule;
		this.versionLexique = versionLexique;
		this.childs = childs;
		this.parent = parent;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Boolean getVisuel() {
		return Visuel;
	}

	public void setVisuel(Boolean visuel) {
		Visuel = visuel;
	}

	public VersionLexique getVersionLexique() {
		return versionLexique;
	}

	public void setVersionLexique(VersionLexique versionLexique) {
		this.versionLexique = versionLexique;
	}

	public Set<Lexique> getChilds() {
		return childs;
	}

	public void setChilds(Set<Lexique> childs) {
		this.childs = childs;
	}

	public Lexique getParent() {
		return parent;
	}

	public void setParent(Lexique parent) {
		this.parent = parent;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public CategorieVehicule getCategorieVehicule() {
		return categorieVehicule;
	}

	public void setCategorieVehicule(CategorieVehicule categorieVehicule) {
		this.categorieVehicule = categorieVehicule;
	}


	public boolean isHaschild() {
		return haschild;
	}


	public void setHaschild(boolean haschild) {
		this.haschild = haschild;
	}
	

}