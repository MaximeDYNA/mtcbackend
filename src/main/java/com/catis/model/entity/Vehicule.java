package com.catis.model.entity;

import java.util.Date;
import java.util.Objects;
import java.util.Set;

import javax.persistence.*;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.model.configuration.JournalData;
import com.catis.objectTemporaire.CarteGriseReceived;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "t_vehicule")
@EntityListeners(AuditingEntityListener.class)
@Audited
@SQLDelete(sql = "UPDATE t_vehicule SET active_status=false WHERE vehicule_id=?")
public class Vehicule extends JournalData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long vehiculeId;
    private String typeVehicule;
    private String carrosserie;
    private int placeAssise;
    private String chassis;
    private Date premiereMiseEnCirculation;
    private int puissAdmin; // Puissance Administrative
    private int poidsTotalCha; // poids total en charge
    private int poidsVide;
    private int chargeUtile; // charge utile
    private int cylindre; // cm3
    private double score;

    @ManyToOne
    private MarqueVehicule marqueVehicule;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Energie energie;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "vehicule")
    @JsonIgnore
    Set<CarteGrise> carteGrise;

    public Vehicule() {

    }

    public Vehicule(CarteGriseReceived cgr) {

        this.typeVehicule = cgr.getTypeVehicule();
        this.carrosserie = cgr.getCarrosserie();
        this.placeAssise = cgr.getPlaces();
        this.chassis = cgr.getChassis();
        this.premiereMiseEnCirculation = cgr.getPremiereMiseEnCirculation();
        this.puissAdmin = cgr.getPuissAdmin();
        this.poidsTotalCha = cgr.getPoidsTotalCha();
        this.poidsVide = cgr.getPoidsVide();
        this.chargeUtile = cgr.getChargeUtile();
        this.cylindre = cgr.getCylindre();


    }

    public Vehicule(Long vehiculeId, String typeVehicule, String carrosserie, int placeAssise, String chassis,
                    Date dateMiseEnCirculation, Date premiereMiseEnCirculation, Energie energie, int cylindre,
                    MarqueVehicule marqueVehicule, Set<CarteGrise> carteGrise) {
        super();
        this.vehiculeId = vehiculeId;
        this.typeVehicule = typeVehicule;
        this.carrosserie = carrosserie;
        this.placeAssise = placeAssise;
        this.chassis = chassis;
        this.premiereMiseEnCirculation = premiereMiseEnCirculation;
        this.energie = energie;
        this.cylindre = cylindre;
        this.marqueVehicule = marqueVehicule;
        this.carteGrise = carteGrise;
    }

    public Long getVehiculeId() {
        return vehiculeId;
    }

    public void setVehiculeId(Long vehiculeId) {
        this.vehiculeId = vehiculeId;
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

    public String getChassis() {
        return chassis;
    }

    public void setChassis(String chassis) {
        this.chassis = chassis;
    }

    public Date getPremiereMiseEnCirculation() {
        return premiereMiseEnCirculation;
    }

    public void setPremiereMiseEnCirculation(Date premiereMiseEnCirculation) {
        this.premiereMiseEnCirculation = premiereMiseEnCirculation;
    }

    public Energie getEnergie() {
        return energie;
    }

    public void setEnergie(Energie energie) {
        this.energie = energie;
    }

    public int getCylindre() {
        return cylindre;
    }

    public void setCylindre(int cylindre) {
        this.cylindre = cylindre;
    }

    public int getPlaceAssise() {
        return placeAssise;
    }

    public void setPlaceAssise(int placeAssise) {
        this.placeAssise = placeAssise;
    }

    public MarqueVehicule getMarqueVehicule() {
        return marqueVehicule;
    }

    public void setMarqueVehicule(MarqueVehicule marqueVehicule) {
        this.marqueVehicule = marqueVehicule;
    }

    public Set<CarteGrise> getCarteGrise() {
        return carteGrise;
    }

    public void setCarteGrise(Set<CarteGrise> carteGrise) {
        this.carteGrise = carteGrise;
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

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vehicule)) return false;
        Vehicule vehicule = (Vehicule) o;
        return Objects.equals(getVehiculeId(), vehicule.getVehiculeId());
    }

}
