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
@Table(name = "t_caisse")
public class Caisse extends JournalData {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long caisse_id;

	private String description;
	private String idOrganisation;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "caisse")
	@JsonIgnore
	private Set<CaissierCaisse> caissiercaisses;

	public Long getCaisse_id() {
		return caisse_id;
	}

	public void setCaisse_id(Long caisse_id) {
		this.caisse_id = caisse_id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIdOrganisation() {
		return idOrganisation;
	}

	public void setIdOrganisation(String idOrganisation) {
		this.idOrganisation = idOrganisation;
	}

	public Set<CaissierCaisse> getCaissiercaisses() {
		return caissiercaisses;
	}

	public Caisse() {

		// TODO Auto-generated constructor stub
	}

	public Caisse(Long caisse_id, String description, String idOrganisation, Set<CaissierCaisse> caissiercaisses) {

		this.caisse_id = caisse_id;
		this.description = description;
		this.idOrganisation = idOrganisation;
		this.caissiercaisses = caissiercaisses;
	}

	public void setCaissiercaisses(Set<CaissierCaisse> caissiercaisses) {
		this.caissiercaisses = caissiercaisses;
	}

}
