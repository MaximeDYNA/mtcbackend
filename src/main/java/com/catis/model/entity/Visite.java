package com.catis.model.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import javax.persistence.*;

import com.catis.model.control.Control;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.model.configuration.JournalData;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "t_visite")
@EntityListeners(AuditingEntityListener.class)
@Audited
public class Visite extends JournalData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idVisite;
    private boolean contreVisite;
    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;
    private int statut;
    private int isConform;


    @Column(columnDefinition = "bit default 1")
    private boolean encours = true;

    @ManyToOne
    @JsonIgnore
    private Caissier caissier;

    @OneToOne(mappedBy = "visite", cascade = CascadeType.ALL)
    @JsonIgnore
    private Inspection inspection;


    @OneToOne(mappedBy = "visite", cascade = CascadeType.ALL)
    @JsonIgnore
    private Vente vente;

    @OneToOne(mappedBy = "visite")
    @JsonIgnore
    private VerbalProcess process;

    @ManyToOne(cascade = CascadeType.ALL)
    private CarteGrise carteGrise;

    @ManyToOne(cascade = CascadeType.ALL)
    private Control control;

    public List<RapportDeVisite> getRapportDeVisites() {
        return rapportDeVisites;
    }

    public void setRapportDeVisites(List<RapportDeVisite> rapportDeVisites) {
        this.rapportDeVisites = rapportDeVisites;
    }

    @OneToMany(cascade = CascadeType.ALL)
    @JsonIgnore
    private List<RapportDeVisite> rapportDeVisites;

    public Visite() {

    }
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
    public Control getControl() {
        return control;
    }

    public void setControl(Control control) {
        this.control = control;
    }

    public Visite(Long idVisite, boolean contreVisite, LocalDateTime dateDebut, LocalDateTime dateFin, int statut,
                  boolean encours, Caissier caissier, Inspection inspection, VerbalProcess process,
                  CarteGrise carteGrise, Control control, List<RapportDeVisite> rapportDeVisites) {
        super();
        this.idVisite = idVisite;
        this.contreVisite = contreVisite;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.statut = statut;
        this.encours = encours;
        this.caissier = caissier;
        this.inspection = inspection;
        this.process = process;
        this.carteGrise = carteGrise;
        this.control = control;
        this.rapportDeVisites = rapportDeVisites;
    }


    public boolean isContreVisite() {
        return contreVisite;
    }

    public void setContreVisite(boolean contreVisite) {
        this.contreVisite = contreVisite;
    }

    public Long getIdVisite() {
        return idVisite;
    }

    public void setIdVisite(Long idVisite) {
        this.idVisite = idVisite;
    }

    public LocalDateTime getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDateTime dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDateTime getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDateTime dateFin) {
        this.dateFin = dateFin;
    }

    public int getStatut() {
        return statut;
    }

    public void setStatut(int statut) {
        this.statut = statut;
    }


    public Inspection getInspection() {
        return inspection;
    }

    public void setInspection(Inspection inspection) {
        this.inspection = inspection;
    }

    public VerbalProcess getProcess() {
        return process;
    }

    public void setProcess(VerbalProcess process) {
        this.process = process;
    }

    public Caissier getCaissier() {
        return caissier;
    }

    public void setCaissier(Caissier caissier) {
        this.caissier = caissier;
    }

    public CarteGrise getCarteGrise() {
        return carteGrise;
    }

    public void setCarteGrise(CarteGrise carteGrise) {
        this.carteGrise = carteGrise;
    }

    public boolean isEncours() {
        return encours;
    }

    public void setEncours(boolean encours) {
        this.encours = encours;
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

    public int getIsConform() {
        return isConform;
    }

    public void setIsConform(int isConform) {
        this.isConform = isConform;
    }

    public String typeRender() {
        if (this.contreVisite) {
            return "CV";
        } else
            return "VI";
    }

    public Vente getVente() {
        return vente;
    }

    public void setVente(Vente vente) {
        this.vente = vente;
    }

    public TestResult getRipage() {
        return ripage;
    }

    public void setRipage(TestResult ripage) {
        this.ripage = ripage;
    }

    public TestResult getSuspension() {
        return suspension;
    }

    public void setSuspension(TestResult suspension) {
        this.suspension = suspension;
    }

    public TestResult getFreinage() {
        return freinage;
    }

    public void setFreinage(TestResult freinage) {
        this.freinage = freinage;
    }

    public TestResult getPollution() {
        return pollution;
    }

    public void setPollution(TestResult pollution) {
        this.pollution = pollution;
    }

    public TestResult getReglophare() {
        return reglophare;
    }

    public void setReglophare(TestResult reglophare) {
        this.reglophare = reglophare;
    }

    public TestResult getVisuel() {
        return visuel;
    }

    public void setVisuel(TestResult visuel) {
        this.visuel = visuel;
    }
}
