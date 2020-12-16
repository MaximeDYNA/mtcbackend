package com.catis.model;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.model.configuration.JournalData;

@Entity
@Table(name = "t_modelevehicule")
@EntityListeners(AuditingEntityListener.class)
public class ModeleVehicule extends JournalData {
	
	@Id
	private Long idModele;
	private String description;
	private String idOganisation;
	

	

	public ModeleVehicule() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	public ModeleVehicule(Long idModele, String description, String idOganisation 
			) {
		super();
		this.idModele = idModele;
		this.description = description;
		this.idOganisation = idOganisation;	
	}


	public Long getIdModele() {
		return idModele;
	}

	public void setIdModele(Long idModele) {
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
