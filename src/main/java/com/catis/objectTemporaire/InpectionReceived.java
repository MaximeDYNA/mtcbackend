package com.catis.objectTemporaire;

import java.util.Date;
import java.util.UUID;


public class InpectionReceived {

    private UUID idInspection;
    private Date dateDebut;
    private Date dateFin;
    private String signature; // chemin image signature du controleur
    private UUID produitId;
    private double kilometrage;
    private String chassis;
    private int essieux;
    private String position;
    private String controleurId;
    private UUID ligneId;
    private UUID visiteId;


    public InpectionReceived() {
        super();
        // TODO Auto-generated constructor stub
    }


    public InpectionReceived(UUID idInspection, Date dateDebut, Date dateFin, String signature, UUID produitId,
                             double kilometrage, String chassis, int essieux, String position, String controleurId, UUID ligneId,
                             UUID visiteId) {
        super();
        this.idInspection = idInspection;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.signature = signature;
        this.produitId = produitId;
        this.kilometrage = kilometrage;
        this.chassis = chassis;
        this.essieux = essieux;
        this.position = position;
        this.controleurId = controleurId;
        this.ligneId = ligneId;
        this.visiteId = visiteId;
    }


    public UUID getIdInspection() {
        return idInspection;
    }


    public void setIdInspection(UUID idInspection) {
        this.idInspection = idInspection;
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


    public String getSignature() {
        return signature;
    }


    public void setSignature(String signature) {
        this.signature = signature;
    }


    public UUID getProduitId() {
        return produitId;
    }


    public void setProduitId(UUID produitId) {
        this.produitId = produitId;
    }


    public double getKilometrage() {
        return kilometrage;
    }


    public void setKilometrage(double kilometrage) {
        this.kilometrage = kilometrage;
    }


    public String getChassis() {
        return chassis;
    }


    public void setChassis(String chassis) {
        this.chassis = chassis;
    }


    public int getEssieux() {
        return essieux;
    }


    public void setEssieux(int essieux) {
        this.essieux = essieux;
    }


    public String getPosition() {
        return position;
    }


    public void setPosition(String position) {
        this.position = position;
    }


    public String getControleurId() {
        return controleurId;
    }


    public void setControleurId(String controleurId) {
        this.controleurId = controleurId;
    }


    public UUID getLigneId() {
        return ligneId;
    }


    public void setLigneId(UUID ligneId) {
        this.ligneId = ligneId;
    }


    public UUID getVisiteId() {
        return visiteId;
    }


    public void setVisiteId(UUID visiteId) {
        this.visiteId = visiteId;
    }


}
