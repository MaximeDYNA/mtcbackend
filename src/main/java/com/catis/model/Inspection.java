package com.catis.model;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.model.configuration.JournalData;
import com.catis.objectTemporaire.InpectionReceived;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "t_inspection")
public class Inspection extends JournalData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idInspection;

    private Date dateDebut;

    private Date dateFin;

    private String signature; // chemin image signature du controleur

    @ManyToOne
    private Produit produit;

    private double kilometrage;

    private String chassis;

    private int essieux;

    private String position;

    @OneToOne(mappedBy = "inspection", cascade = CascadeType.ALL)
    private MesureVisuel mesureVisuel;

    @ManyToOne
    private Controleur controleur;

    @ManyToOne
    private Ligne ligne;

    @OneToOne(cascade = CascadeType.ALL)
    private Visite visite;

    @OneToMany(mappedBy = "inspection", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<GieglanFile> gieglanFiles;

    @ManyToMany
    private List<Lexique> lexiques;

    /*
     * @ManyToMany(mappedBy = "inspections") private Set<Lexique> lexiques;
     */
    public Inspection() {
        // TODO Auto-generated constructor stub
    }

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

    public List<Lexique> getLexiques() {
        return lexiques;
    }

    public void setLexiques(List<Lexique> lexiques) {
        this.lexiques = lexiques;
    }

    public void addLexique(Lexique lexique) {
        this.lexiques.add(lexique);
    }

    public Inspection(Date dateDebut, Date dateFin, String signature, Produit produit,
                      double kilometrage, String chassis, int essieux, String position, Controleur controleur,
                       Ligne ligne, Visite visite, Set<GieglanFile> gieglanFiles, List<Lexique> lexiques
    ) {
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.signature = signature;
        this.produit = produit;
        this.kilometrage = kilometrage;
        this.chassis = chassis;
        this.essieux = essieux;
        this.position = position;
        this.controleur = controleur;

        this.ligne = ligne;
        this.visite = visite;
        this.gieglanFiles = gieglanFiles;
        this.lexiques = lexiques;
    }

    public Long getIdInspection() {
        return idInspection;
    }

    public void setIdInspection(Long idInspection) {
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

    public Produit getProduit() {
        return produit;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
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

    public Controleur getControleur() {
        return controleur;
    }

    public void setControleur(Controleur controleur) {
        this.controleur = controleur;
    }

    public Ligne getLigne() {
        return ligne;
    }

    public void setLigne(Ligne ligne) {
        this.ligne = ligne;
    }

    public Visite getVisite() {
        return visite;
    }

    public void setVisite(Visite visite) {
        this.visite = visite;
    }

    public Set<GieglanFile> getGieglanFiles() {
        return gieglanFiles;
    }

    public void setGieglanFiles(Set<GieglanFile> gieglanFiles) {
        this.gieglanFiles = gieglanFiles;
    }



    public MesureVisuel getMesureVisuel() {
        return mesureVisuel;
    }

    public void setMesureVisuel(MesureVisuel mesureVisuel) {
        this.mesureVisuel = mesureVisuel;
    }
}
