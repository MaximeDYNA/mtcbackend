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
@Table(name = "t_pays")
public class Pays extends JournalData{
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long paysId;
	private String nomPays;
	private String description;
	
	@OneToMany(mappedBy="pays")
	@JsonIgnore
	Set <DivisionPays> divisionPays;

	public Pays() {
		
	}
  
	public Pays(Long paysId, String nomPays, Set<DivisionPays> divisionPays) {
		super();
		this.paysId = paysId;
		this.nomPays = nomPays;
		this.divisionPays = divisionPays;
	}


	public String getNomPays() {
		return nomPays;
	}

	public void setNomPays(String nomPays) {
		this.nomPays = nomPays;
	}



	public Set<DivisionPays> getDivisionPays() {
		return divisionPays;
	}



	public void setDivisionPays(Set<DivisionPays> divisionPays) {
		this.divisionPays = divisionPays;
	}

	public Long getPaysId() {
		return paysId;
	}

	public void setPaysId(Long paysId) {
		this.paysId = paysId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	
	
}
