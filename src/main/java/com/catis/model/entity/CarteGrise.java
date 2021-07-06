package com.catis.model.entity;

import java.util.Date;
import java.util.Set;

import javax.persistence.*;

import com.catis.model.control.Control;
import com.catis.objectTemporaire.CarteGrisePOJO;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.model.configuration.JournalData;
import com.catis.objectTemporaire.CarteGriseReceived;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "t_cartegrise")
@EntityListeners(AuditingEntityListener.class)
@Audited
@SQLDelete(sql = "UPDATE t_cartegrise SET active_status=false WHERE carte_grise_id=?")
public class CarteGrise extends JournalData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long carteGriseId;

    private String numImmatriculation;

    private String preImmatriculation;// immatriculation précédente

    private Date dateDebutValid; // debut de validité

    private Date dateFinValid;// fin de validité
    private String ssdt_id;
    private String commune;
    private double montantPaye;
    private boolean vehiculeGage; // véhicule gagé
    private String genreVehicule;
    private String enregistrement;

    private Date dateDelivrance;
    private String lieuDedelivrance;// lieu de délivrance
    private String centre_ssdt;

    @ManyToOne
    private ProprietaireVehicule proprietaireVehicule;

    @ManyToOne
    private Vehicule vehicule;

    @ManyToOne
    private Produit produit;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "carteGrise")
    @JsonIgnore
    Set<Visite> visites;

    @OneToMany(mappedBy = "carteGrise")
    @JsonIgnore
    private Set<Control> controls;

    public CarteGrise(CarteGriseReceived c) {

        this.numImmatriculation = c.getNumImmatriculation();
        this.preImmatriculation = c.getPreImmatriculation();
        this.dateDebutValid = c.getDateDebutValid();
        this.dateFinValid = c.getDateFinValid();
        this.ssdt_id = c.getSsdt_id();
        this.commune = c.getCommune();
        this.montantPaye = c.getMontantPaye();
        this.vehiculeGage = c.isVehiculeGage();
        this.genreVehicule = c.getGenreVehicule();

        this.enregistrement = c.getEnregistrement();
        this.dateDelivrance = c.getDateDelivrance();
        this.lieuDedelivrance = c.getLieuDedelivrance();
        this.centre_ssdt = c.getCentre_ssdt();

    }
    public CarteGrise(CarteGrisePOJO c) {

        this.carteGriseId = c.getCarteGriseId();
        this.numImmatriculation = c.getNumImmatriculation();
        this.preImmatriculation = c.getPreImmatriculation();
        this.dateDebutValid = c.getDateDebutValid();
        this.dateFinValid = c.getDateFinValid();
        this.ssdt_id = c.getSsdt_id();
        this.commune = c.getCommune();
        this.montantPaye = c.getMontantPaye();
        this.vehiculeGage = c.isVehiculeGage();
        this.genreVehicule = c.getGenreVehicule();

        this.enregistrement = c.getEnregistrement();
        this.dateDelivrance = c.getDateDelivrance();
        this.lieuDedelivrance = c.getLieuDedelivrance();
        this.centre_ssdt = c.getCentre_ssdt();

    }


    public CarteGrise(Long carteGriseId, String numImmatriculation, String preImmatriculation, Date dateDebutValid,
                      Date dateFinValid, String ssdt_id, String commune, double montantPaye, boolean vehiculeGage,
                      String genreVehicule, String enregistrement, Date dateDelivrance, String lieuDedelivrance,
                      String centre_ssdt, ProprietaireVehicule proprietaireVehicule, Vehicule vehicule, Produit produit,
                      Set<Visite> visites, Set<Control> controls) {
        super();
        this.carteGriseId = carteGriseId;
        this.numImmatriculation = numImmatriculation;
        this.preImmatriculation = preImmatriculation;
        this.dateDebutValid = dateDebutValid;
        this.dateFinValid = dateFinValid;
        this.ssdt_id = ssdt_id;
        this.commune = commune;
        this.montantPaye = montantPaye;
        this.vehiculeGage = vehiculeGage;
        this.genreVehicule = genreVehicule;
        this.enregistrement = enregistrement;
        this.dateDelivrance = dateDelivrance;
        this.lieuDedelivrance = lieuDedelivrance;
        this.centre_ssdt = centre_ssdt;
        this.proprietaireVehicule = proprietaireVehicule;
        this.vehicule = vehicule;
        this.produit = produit;
        this.visites = visites;
        this.controls = controls;
    }

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
                      String genreVehicule, String enregistrement, Date dateDelivrance, String lieuDedelivrance,
                      String centre_ssdt, ProprietaireVehicule proprietaireVehicule, Vehicule vehicule, Produit produit,
                      Set<Visite> visites) {
        this.carteGriseId = carteGriseId;
        this.numImmatriculation = numImmatriculation;
        this.preImmatriculation = preImmatriculation;
        this.dateDebutValid = dateDebutValid;
        this.dateFinValid = dateFinValid;
        this.ssdt_id = ssdt_id;
        this.commune = commune;
        this.montantPaye = montantPaye;
        this.vehiculeGage = vehiculeGage;
        this.genreVehicule = genreVehicule;
        this.enregistrement = enregistrement;
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
        return commune;
    }

    public void setCommune(String commune) {
        this.commune = commune;
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

    public Date getDateDelivrance() {
        return dateDelivrance;
    }

    public Set<Control> getControls() {
        return controls;
    }

    public void setControls(Set<Control> controls) {
        this.controls = controls;
    }

}
