package com.catis.objectTemporaire;

import com.catis.model.control.Control;
import com.catis.model.entity.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class VisiteDate {

    private UUID idVisite;
    private boolean contreVisite;
    private Date dateDebut;
    private Date dateFin;
    private int statut;
    private Organisation organisation;
    private boolean encours;
    private Caissier caissier;
    private Inspection inspection;
    private VerbalProcess process;
    private CarteGrise carteGrise;
    private Control control;
    private List<RapportDeVisite> rapportDeVisites;

    public VisiteDate() {
    }

    public VisiteDate(Visite visite) {
        this.idVisite = visite.getIdVisite();
        this.contreVisite = visite.isContreVisite();
        this.dateDebut = convertLocalDateTimeToDateUsingInstant(visite.getDateDebut());
        this.dateFin = convertLocalDateTimeToDateUsingInstant(visite.getDateFin());
        this.statut = visite.getStatut();
        this.organisation = visite.getOrganisation();
        this.encours = visite.isEncours();
        this.caissier = visite.getCaissier();
        this.inspection = visite.getInspection();
        this.process = visite.getProcess();
        this.carteGrise = visite.getCarteGrise();
        this.control = visite.getControl();
        this.rapportDeVisites = visite.getRapportDeVisites();
    }
    Date convertLocalDateTimeToDateUsingInstant(LocalDateTime dateToConvert) {
        return java.util.Date
                .from(dateToConvert.atZone(ZoneId.systemDefault())
                        .toInstant());
    }

    public UUID getIdVisite() {
        return idVisite;
    }

    public void setIdVisite(UUID idVisite) {
        this.idVisite = idVisite;
    }

    public boolean isContreVisite() {
        return contreVisite;
    }

    public void setContreVisite(boolean contreVisite) {
        this.contreVisite = contreVisite;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public int getStatut() {
        return statut;
    }

    public void setStatut(int statut) {
        this.statut = statut;
    }

    public Organisation getOrganisation() {
        return organisation;
    }

    public void setOrganisation(Organisation organisation) {
        this.organisation = organisation;
    }

    public boolean isEncours() {
        return encours;
    }

    public void setEncours(boolean encours) {
        this.encours = encours;
    }

    public Caissier getCaissier() {
        return caissier;
    }

    public void setCaissier(Caissier caissier) {
        this.caissier = caissier;
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

    public CarteGrise getCarteGrise() {
        return carteGrise;
    }

    public void setCarteGrise(CarteGrise carteGrise) {
        this.carteGrise = carteGrise;
    }

    public Control getControl() {
        return control;
    }

    public void setControl(Control control) {
        this.control = control;
    }

    public List<RapportDeVisite> getRapportDeVisites() {
        return rapportDeVisites;
    }

    public void setRapportDeVisites(List<RapportDeVisite> rapportDeVisites) {
        this.rapportDeVisites = rapportDeVisites;
    }
}
