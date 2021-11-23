package com.catis.model.entity;

import java.util.*;

import javax.persistence.*;

import com.catis.model.control.GieglanFile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.model.configuration.JournalData;
import com.catis.objectTemporaire.InpectionReceived;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "t_inspection")
@Audited
@SQLDelete(sql = "UPDATE t_inspection SET active_status=false WHERE idInspection=?")
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class Inspection extends JournalData {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID idInspection;

    private Date dateDebut;

    private Date dateFin;

    private boolean visibleToTab = true;

    private String signature; // chemin image signature du controleur

    @ManyToOne
    private Produit produit;

    private double kilometrage;

    private String chassis;

    private int essieux;

    private String position;

    private UUID visiteIdReseted;

    private double distancePercentage;

    private String bestPlate;

    @ManyToOne(cascade = CascadeType.ALL)
    private Controleur controleur;

    @ManyToOne
    private Ligne ligne;

    @OneToOne(cascade = CascadeType.ALL)
    private Visite visite;

    @OneToMany(mappedBy = "inspection", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<GieglanFile> gieglanFiles =new HashSet<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Lexique> lexiques;

    /*
     * @ManyToMany(mappedBy = "inspections") private Set<Lexique> lexiques;
     */

    public Inspection(InpectionReceived i) {

        this.idInspection = i.getIdInspection();
        this.dateDebut = i.getDateDebut();
        this.dateFin = i.getDateFin();
        this.signature = i.getSignature();
        this.kilometrage = i.getKilometrage();
        this.chassis = i.getChassis();
        this.essieux = i.getEssieux();
        this.position = i.getPosition();
    }
    public void addLexique(Lexique lexique) {
        this.lexiques.add(lexique);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Inspection)) return false;
        Inspection that = (Inspection) o;
        return Objects.equals(getIdInspection(), that.getIdInspection());
    }


}
