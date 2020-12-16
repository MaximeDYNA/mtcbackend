package com.catis.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.model.GieglanFile.StatusType;
import com.catis.model.configuration.JournalData;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity @Table(name = "t_valeurtest")
@EntityListeners(AuditingEntityListener.class)
public class ValeurTest extends JournalData{

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idValeurTest;

	private String code;

	private String valeur;

	private Integer crc;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "varchar(255) default 'INITIALIZED'")
	private StatusType status;

	private String description;

	@ManyToOne
	private Organisation organisation;
	
	@ManyToOne
	private Mesure mesure;

	@ManyToOne
	private GieglanFile gieglanFile;

	public ValeurTest() {
		
		// TODO Auto-generated constructor stub
	}

	

	public Long getIdValeurTest() {
		return idValeurTest;
	}

	public void setIdValeurTest(Long idValeurTest) {
		this.idValeurTest = idValeurTest;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getValeur() {
		return valeur;
	}

	public void setValeur(String valeur) {
		this.valeur = valeur;
	}

	public Integer getCrc() {
		return crc;
	}

	public void setCrc(Integer crc) {
		this.crc = crc;
	}

	public StatusType getStatus() {
		return status;
	}

	public void setStatus(StatusType status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	


	public Organisation getOrganisation() {
		return organisation;
	}



	public void setOrganisation(Organisation organisation) {
		this.organisation = organisation;
	}



	public Mesure getMesure() {
		return mesure;
	}

	public void setMesure(Mesure mesure) {
		this.mesure = mesure;
	}

	public GieglanFile getGieglanFile() {
		return gieglanFile;
	}

	public void setGieglanFile(GieglanFile gieglanFile) {
		this.gieglanFile = gieglanFile;
	}
	
	
}


