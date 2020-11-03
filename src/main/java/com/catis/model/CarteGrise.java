package com.catis.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="t_cartegrise")
public class CarteGrise {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long carteGriseId;
	private String numImmatriculation;
	private String preImmatriculation;// immatriculation précédente
	private Date dateDebutValid; //debut de validité
	private Date dateFinValid;// fin de validité
	private String ssdt_id;
	private String Commune;
	private double montantPaye;
	private boolean vehiculeGage; // véhicule gagé
	private String genreVehicule;
	private String marqueVehicule;
	private String typeVehicule;
	private String carrosserie;
	private String enregistrement;
	private String chassis;
	private Date dateMiseEnCirculation;
	private Date premiereMiseEnCirculation;
	private String energie;
	private int cylindre; //cm3
	private int puissAdmin; // Puissance Administrative
	private int poidsTotalCha; // poids total en charge
	private int poidsVide;
	private int chargeUtile; // charge utile
	private Date dateDelivrance;
	private String lieuDedelivrance;// lieu de délivrance
	private String centre_ssdt;
	
	@ManyToOne
	@JoinColumn(name="idProprietaireVehicule")
	private ProprietaireVehicule proprietaireVehicule;
	
	@ManyToOne
	@JoinColumn(name="idVehicule")
	private Vehicule vehicule;
	
	@ManyToOne
	private Produit produit;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="carteGrise")
	@JsonIgnore
	Set<Visite> visites; 
	
	public String getEnregistrement() {
		return enregistrement;
	}

	public void setEnregistrement(String enregistrement) {
		this.enregistrement = enregistrement;
	}

	public Set<Visite> getVisites() {
		return visites;
	}

	public void setVisites(Set<Visite> visites) {
		this.visites = visites;
	}

	public CarteGrise() {
	}

	

	

	
	public CarteGrise(Long carteGriseId, String numImmatriculation, String preImmatriculation, Date dateDebutValid,
			Date dateFinValid, String ssdt_id, String commune, double montantPaye, boolean vehiculeGage,
			String genreVehicule, String marqueVehicule, String typeVehicule, String carrosserie, String enregistrement,
			String chassis, Date dateMiseEnCirculation, Date premiereMiseEnCirculation, String energie, int cylindre,
			int puissAdmin, int poidsTotalCha, int poidsVide, int chargeUtile, Date dateDelivrance,
			String lieuDedelivrance, String centre_ssdt, ProprietaireVehicule proprietaireVehicule, Vehicule vehicule,
			Produit produit, Set<Visite> visites) {
		super();
		this.carteGriseId = carteGriseId;
		this.numImmatriculation = numImmatriculation;
		this.preImmatriculation = preImmatriculation;
		this.dateDebutValid = dateDebutValid;
		this.dateFinValid = dateFinValid;
		this.ssdt_id = ssdt_id;
		Commune = commune;
		this.montantPaye = montantPaye;
		this.vehiculeGage = vehiculeGage;
		this.genreVehicule = genreVehicule;
		this.marqueVehicule = marqueVehicule;
		this.typeVehicule = typeVehicule;
		this.carrosserie = carrosserie;
		this.enregistrement = enregistrement;
		this.chassis = chassis;
		this.dateMiseEnCirculation = dateMiseEnCirculation;
		this.premiereMiseEnCirculation = premiereMiseEnCirculation;
		this.energie = energie;
		this.cylindre = cylindre;
		this.puissAdmin = puissAdmin;
		this.poidsTotalCha = poidsTotalCha;
		this.poidsVide = poidsVide;
		this.chargeUtile = chargeUtile;
		this.dateDelivrance = dateDelivrance;
		this.lieuDedelivrance = lieuDedelivrance;
		this.centre_ssdt = centre_ssdt;
		this.proprietaireVehicule = proprietaireVehicule;
		this.vehicule = vehicule;
		this.produit = produit;
		this.visites = visites;
	}

	public Long getCarteGriseId() {
		return carteGriseId;
	}

	public void setCarteGriseId(Long carteGriseId) {
		this.carteGriseId = carteGriseId;
	}

	public String getNumImmatriculation() {
		return numImmatriculation;
	}

	public void setNumImmatriculation(String numImmatriculation) {
		this.numImmatriculation = numImmatriculation;
	}

	public String getPreImmatriculation() {
		return preImmatriculation;
	}

	public void setPreImmatriculation(String preImmatriculation) {
		this.preImmatriculation = preImmatriculation;
	}

	public Date getDateDebutValid() {
		return dateDebutValid;
	}

	public void setDateDebutValid(Date dateDebutValid) {
		this.dateDebutValid = dateDebutValid;
	}

	public Date getDateFinValid() {
		return dateFinValid;
	}

	public void setDateFinValid(Date dateFinValid) {
		this.dateFinValid = dateFinValid;
	}

	public String getSsdt_id() {
		return ssdt_id;
	}

	public void setSsdt_id(String ssdt_id) {
		this.ssdt_id = ssdt_id;
	}

	public String getCommune() {
		return Commune;
	}

	public void setCommune(String commune) {
		Commune = commune;
	}

	public double getMontantPaye() {
		return montantPaye;
	}

	public void setMontantPaye(double montantPaye) {
		this.montantPaye = montantPaye;
	}

	public boolean isVehiculeGage() {
		return vehiculeGage;
	}

	public void setVehiculeGage(boolean vehiculeGage) {
		this.vehiculeGage = vehiculeGage;
	}

	public String getGenreVehicule() {
		return genreVehicule;
	}

	public void setGenreVehicule(String genreVehicule) {
		this.genreVehicule = genreVehicule;
	}

	public String getMarqueVehicule() {
		return marqueVehicule;
	}

	public void setMarqueVehicule(String marqueVehicule) {
		this.marqueVehicule = marqueVehicule;
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

	public String getEnrgistrement() {
		return enregistrement;
	}

	public void setEnrgistrement(String enregistrement) {
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

	public int getPuissAdmin() {
		return puissAdmin;
	}

	public void setPuissAdmin(int puissAdmin) {
		this.puissAdmin = puissAdmin;
	}

	public int getPoidsTotalCha() {
		return poidsTotalCha;
	}

	public void setPoidsTotalCha(int poidsTotalCha) {
		this.poidsTotalCha = poidsTotalCha;
	}

	public int getPoidsVide() {
		return poidsVide;
	}

	public void setPoidsVide(int poidsVide) {
		this.poidsVide = poidsVide;
	}

	public int getChargeUtile() {
		return chargeUtile;
	}

	public void setChargeUtile(int chargeUtile) {
		this.chargeUtile = chargeUtile;
	}

	public Date getDateDelivrance() {
		return dateDelivrance;
	}

	public void setDateDelivrance(Date dateDelivrance) {
		this.dateDelivrance = dateDelivrance;
	}

	public String getLieuDedelivrance() {
		return lieuDedelivrance;
	}

	public void setLieuDedelivrance(String lieuDedelivrance) {
		this.lieuDedelivrance = lieuDedelivrance;
	}

	public String getCentre_ssdt() {
		return centre_ssdt;
	}

	public void setCentre_ssdt(String centre_ssdt) {
		this.centre_ssdt = centre_ssdt;
	}

	public ProprietaireVehicule getProprietaireVehicule() {
		return proprietaireVehicule;
	}

	public void setProprietaireVehicule(ProprietaireVehicule proprietaireVehicule) {
		this.proprietaireVehicule = proprietaireVehicule;
	}

	public Vehicule getVehicule() {
		return vehicule;
	}

	public void setVehicule(Vehicule vehicule) {
		this.vehicule = vehicule;
	}

	public Produit getProduit() {
		return produit;
	}

	public void setProduit(Produit produit) {
		this.produit = produit;
	}
	
	
}
