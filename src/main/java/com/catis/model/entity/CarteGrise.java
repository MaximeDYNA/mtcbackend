package com.catis.model.entity;

import java.util.Date;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import javax.persistence.*;

import com.catis.model.control.Control;
import com.catis.objectTemporaire.CarteGrisePOJO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.model.configuration.JournalData;
import com.catis.objectTemporaire.CarteGriseReceived;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@Table(name = "t_cartegrise")
@EntityListeners(AuditingEntityListener.class)
@Audited
@SQLDelete(sql = "UPDATE t_cartegrise SET active_status=false WHERE id=?")
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class CarteGrise extends JournalData {

    @Id
    @GeneratedValue(generator = "UUID")
    @Type(type="uuid-char")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID carteGriseId;

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
    private String type;
    private Date dateDelivrance;
    private String lieuDedelivrance;// lieu de délivrance
    private String centre_ssdt;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE },fetch = FetchType.LAZY)
    private ProprietaireVehicule proprietaireVehicule;


    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private Vehicule vehicule;

   
    @ManyToOne(fetch = FetchType.LAZY)
    private Produit produit;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "carteGrise")
    @JsonIgnore
    Set<Visite> visites;

    @OneToMany(mappedBy = "carteGrise",fetch = FetchType.LAZY)
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
        this.type = c.getType();

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



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CarteGrise)) return false;
        CarteGrise that = (CarteGrise) o;
        return Double.compare(that.getMontantPaye(), getMontantPaye()) == 0 &&
                isVehiculeGage() == that.isVehiculeGage() &&
                Objects.equals(getCarteGriseId(), that.getCarteGriseId());
    }


}
