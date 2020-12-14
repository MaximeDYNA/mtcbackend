package com.catis.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.model.GieglanFile.StatusType;
import com.catis.model.configuration.JournalData;

@Entity @Table(name = "t_valeurtest")
@EntityListeners(AuditingEntityListener.class)
public class ValeurTest extends JournalData{

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "uuid")
	private Long idValeurTest;

	private String code;

	private String valeur;

	private Integer crc;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "varchar(255) default 'INITIALIZED'")
	private StatusType status;

	private String description;

	private String idOrganisation;
	
	@ManyToOne
	private Mesure mesure;

	@ManyToOne
	private GieglanFile gieglanFile;

	public ValeurTest() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ValeurTest(Long idValeurTest, String code, String valeur, Integer crc, StatusType status, String description,
			String idOrganisation, Mesure mesure, GieglanFile gieglanFile) {
		super();
		this.idValeurTest = idValeurTest;
		this.code = code;
		this.valeur = valeur;
		this.crc = crc;
		this.status = status;
		this.description = description;
		this.idOrganisation = idOrganisation;
		this.mesure = mesure;
		this.gieglanFile = gieglanFile;
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

	public String getIdOrganisation() {
		return idOrganisation;
	}

	public void setIdOrganisation(String idOrganisation) {
		this.idOrganisation = idOrganisation;
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


