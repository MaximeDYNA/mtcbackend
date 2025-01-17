package com.catis.model.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import javax.persistence.*;

import com.catis.model.control.Control;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.model.configuration.JournalData;
import com.fasterxml.jackson.annotation.JsonIgnore;

// @ToString
@Entity
@Table(name = "t_visite")
@EntityListeners(AuditingEntityListener.class)
@Audited
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class Visite extends JournalData {

    @Id
    @GeneratedValue(generator = "UUID")
    @Type(type="uuid-char")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID idVisite;
    private boolean contreVisite;
    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;
    private int statut;
    private int isConform;
    private String certidocsId;

    @Column(columnDefinition = "bit default 1")
    private boolean encours = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Caissier caissier;

    @OneToOne(mappedBy = "visite", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonIgnore
    private Inspection inspection;


    @OneToOne(mappedBy = "visite", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonIgnore
    private Vente vente;

    private String document;


    @OneToOne(mappedBy = "visite",fetch = FetchType.LAZY)
    @JsonIgnore
    private VerbalProcess process;

 
  
    @ManyToOne(cascade = CascadeType.MERGE,fetch = FetchType.LAZY)
    @JsonIgnore
    private CarteGrise carteGrise;


    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonIgnore
    private Control control;

    public List<RapportDeVisite> getRapportDeVisites() {
        return rapportDeVisites;
    }

    public void setRapportDeVisites(List<RapportDeVisite> rapportDeVisites) {
        this.rapportDeVisites = rapportDeVisites;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<RapportDeVisite> rapportDeVisites;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(255) default 'PENDING'")
    private TestResult ripage;
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(255) default 'PENDING'")
    private TestResult suspension;
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(255) default 'PENDING'")
    private TestResult freinage;
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(255) default 'PENDING'")
    private TestResult pollution;
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(255) default 'PENDING'")
    private TestResult reglophare;
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(255) default 'PENDING'")
    private TestResult visuel;


    public enum TestResult{
        PENDING, SUCCESS, ERROR
    };


    // flemming added
    public void resetState() {
        this.ripage = TestResult.PENDING;
        this.suspension = TestResult.PENDING;
        this.freinage = TestResult.PENDING;
        this.pollution = TestResult.PENDING;
        this.reglophare = TestResult.PENDING;
        this.visuel = TestResult.PENDING;
    }

    // flemming added
    public void clearProcessAndRapports() {
        if (this.process != null) {
            this.process.setVisite(null);
            this.process = null;
        }
        if (this.rapportDeVisites != null) {
            for (RapportDeVisite rapport : this.rapportDeVisites) {
                rapport.setVisite(null);
            }
            this.rapportDeVisites.clear();
        }
    }

    public String statutRender(int code) {
        if (code == 0) {
            return "maj";
        } else if (code == 1) {
            return "A inspecter";
        } else if (code == 2) {
            return "En cours test";
        } else if (code == 3) {
            return "A signer";
        } else if (code == 4) {
            return "En Attente conformité";
        } else if (code == 5) {
            return "Non conforme";
        } else if (code == 6) {
            return "A imprimer";
        } else if (code == 7) {
            return "Refusé";
        } else if (code == 8) {
            return "A certifier";
        } else if (code == 9) {
            return "Accepté";
        } else if (code == 10) {
            return "A approuver";
        } else {
            return "erreur";
        }

    }

    public String statutRender() {
        if (this.statut == 0) {
            return "maj";
        } else if (this.statut == 1) {
            return "A inspecter";
        } else if (this.statut == 2) {
            return "En cours test";
        } else if (this.statut == 3) {
            return "A signer";
        } else if (this.statut == 4) {
            return "En Attente conformité";
        } else if (this.statut == 5) {
            return "Non conforme";
        } else if (this.statut == 6) {
            return "A imprimer";
        } else if (this.statut == 7) {
            return "Refusé";
        } else if (this.statut == 8) {
            return "A certifier";
        } else if (this.statut == 9) {
            return "Accepté";
        } else if (this.statut == 10) {
            return "A approuver";
        } else {
            return "erreur";
        }

    }

    public String typeRender() {
        if (this.contreVisite) {
            return "CV";
        } else
            return "VI";
    }


}
