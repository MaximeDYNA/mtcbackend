package com.catis.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "t_valeurtest")
public class ValeurTest {

	@Id
	@Column(name = "uuid", nullable = false, updatable = false)
	private UUID idValeurTest;
	
	private String code;
	private double valeur;
	private String description;
	private String idOrganisation;
	
	@ManyToOne
	@JoinColumn(name="idMesure")
	private Mesure mesure;
	
	@ManyToOne
	@JoinColumn(name="idMachine")
	private Machine machine;
	
	public ValeurTest() {
		
	}

	


	public ValeurTest(UUID idValeurTest, String code, double valeur, String description, String idOrganisation,
			Mesure mesure, Machine machine) {
		super();
		this.idValeurTest = idValeurTest;
		this.code = code;
		this.valeur = valeur;
		this.description = description;
		this.idOrganisation = idOrganisation;
		this.mesure = mesure;
		this.machine = machine;
	}



	public UUID getIdValeurTest() {
		return idValeurTest;
	}




	public void setIdValeurTest(UUID idValeurTest) {
		this.idValeurTest = idValeurTest;
	}




	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public double getValeur() {
		return valeur;
	}
	public void setValeur(double valeur) {
		this.valeur = valeur;
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



	public Machine getMachine() {
		return machine;
	}



	public void setMachine(Machine machine) {
		this.machine = machine;
	}
	
	
	
	
}
