package com.catis.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "t_modelevehicule")
public class ModeleVehicule {
	@Id
	private String idModele;
	private String description;
	private String idOganisation;
	

	

	public ModeleVehicule() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	public ModeleVehicule(String idModele, String description, String idOganisation 
			) {
		super();
		this.idModele = idModele;
		this.description = description;
		this.idOganisation = idOganisation;
		
		
	}


	public String getIdModele() {
		return idModele;
	}

	public void setIdModele(String idModele) {
		this.idModele = idModele;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIdOganisation() {
		return idOganisation;
	}

	public void setIdOganisation(String idOganisation) {
		this.idOganisation = idOganisation;
	}

	

	
	
}
