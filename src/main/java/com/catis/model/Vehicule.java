package com.catis.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="t_vehicule")
public class Vehicule {
	@Id
	private String idVehicule;
	private String typeVehicule;
	private String carrosserie;
	private String enregistrement;
	private String chassis;
	private Date dateMiseEnCirculation;
	private Date premiereMiseEnCirculation;
	private String energie;
	private int cylindre; //cm3
	
	@ManyToOne
	@JoinColumn(name="idModele")
	private ModeleVehicule modeleVehicule;
	
	

	public Vehicule(String idVehicule, String typeVehicule, String carrosserie, String enregistrement, String chassis,
			Date dateMiseEnCirculation, Date premiereMiseEnCirculation, String energie, int cylindre,
			ModeleVehicule modeleVehicule) {
		super();
		this.idVehicule = idVehicule;
		this.typeVehicule = typeVehicule;
		this.carrosserie = carrosserie;
		this.enregistrement = enregistrement;
		this.chassis = chassis;
		this.dateMiseEnCirculation = dateMiseEnCirculation;
		this.premiereMiseEnCirculation = premiereMiseEnCirculation;
		this.energie = energie;
		this.cylindre = cylindre;
		this.modeleVehicule = modeleVehicule;
	}

	public Vehicule() {
		
	}
	
	public String getIdVehicule() {
		return idVehicule;
	}
	public void setIdVehicule(String idVehicule) {
		this.idVehicule = idVehicule;
	}
	
	public String getTypeVehicule() {
		return typeVehicule;
	}
	public void setTypeVehicule(String typeVehicule) {
		this.typeVehicule = typeVehicule;
	}
	public String getCarrosserie() {
		return carrosserie;
	}
	public void setCarrosserie(String carrosserie) {
		this.carrosserie = carrosserie;
	}
	public String getEnregistrement() {
		return enregistrement;
	}
	public void setEnregistrement(String enregistrement) {
		this.enregistrement = enregistrement;
	}
	public String getChassis() {
		return chassis;
	}
	public void setChassis(String chassis) {
		this.chassis = chassis;
	}
	public Date getDateMiseEnCirculation() {
		return dateMiseEnCirculation;
	}
	public void setDateMiseEnCirculation(Date dateMiseEnCirculation) {
		this.dateMiseEnCirculation = dateMiseEnCirculation;
	}
	public Date getPremiereMiseEnCirculation() {
		return premiereMiseEnCirculation;
	}
	public void setPremiereMiseEnCirculation(Date premiereMiseEnCirculation) {
		this.premiereMiseEnCirculation = premiereMiseEnCirculation;
	}
	public String getEnergie() {
		return energie;
	}
	public void setEnergie(String energie) {
		this.energie = energie;
	}
	public int getCylindre() {
		return cylindre;
	}
	public void setCylindre(int cylindre) {
		this.cylindre = cylindre;
	}

	public ModeleVehicule getModeleVehicule() {
		return modeleVehicule;
	}

	public void setModeleVehicule(ModeleVehicule modeleVehicule) {
		this.modeleVehicule = modeleVehicule;
	}

	
	
	
}
