package com.catis.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "t_marquevehicule")
public class MarqueVehicule {
	@Id
	private String idMarqueVehicule;
	private String description;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="marqueVehicule")
	@JsonIgnore
	private Set<ModeleVehicule> modeleVehicule;

	public MarqueVehicule() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MarqueVehicule(String idMarqueVehicule, String description, Set<ModeleVehicule> modeleVehicule) {
		super();
		this.idMarqueVehicule = idMarqueVehicule;
		this.description = description;
		this.modeleVehicule = modeleVehicule;
	}

	public String getIdMarqueVehicule() {
		return idMarqueVehicule;
	}

	public void setIdMarqueVehicule(String idMarqueVehicule) {
		this.idMarqueVehicule = idMarqueVehicule;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<ModeleVehicule> getModeleVehicule() {
		return modeleVehicule;
	}

	public void setModeleVehicule(Set<ModeleVehicule> modeleVehicule) {
		this.modeleVehicule = modeleVehicule;
	}
	
	
	
}
