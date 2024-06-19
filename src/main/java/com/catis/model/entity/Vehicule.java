package com.catis.model.entity;

import java.util.Date;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Type;
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
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class Vehicule extends JournalData {
    @Id
    @GeneratedValue(generator = "UUID")
    @Type(type="uuid-char")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID vehiculeId;
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

    @ManyToOne(fetch = FetchType.LAZY)
    private MarqueVehicule marqueVehicule;

   
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE},fetch = FetchType.LAZY)
    private Energie energie;    

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "vehicule")
    @JsonIgnore
    Set<CarteGrise> carteGrise;



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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vehicule)) return false;
        Vehicule vehicule = (Vehicule) o;
        return Objects.equals(getVehiculeId(), vehicule.getVehiculeId());
    }

}
