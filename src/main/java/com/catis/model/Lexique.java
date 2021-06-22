package com.catis.model;

import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.model.configuration.JournalData;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Audited
public class Lexique extends JournalData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String libelle;

    private String code;

    private Boolean Visuel;

    @ManyToMany(mappedBy = "lexiques")
    @JsonIgnore
    private Set<Client> clients;

    @OneToMany(mappedBy = "lexique")
    @JsonIgnore
    private Set<Seuil> seuils;

    @ManyToOne
    private CategorieVehicule categorieVehicule;

    @ManyToOne
    private VersionLexique versionLexique;

    @OneToMany(mappedBy = "parent")
    @JsonIgnore
    private Set<Lexique> childs;


    @ManyToOne
    private Lexique parent;

    @ManyToMany(mappedBy = "lexiques")
    private List<Inspection> inspections;

    @ManyToOne
    private Classification classification;

    private Boolean haschild;

    public Lexique() {
        super();
        // TODO Auto-generated constructor stub
    }

    public Lexique(Long id, String libelle, String code, Boolean visuel, Client client,
                   CategorieVehicule categorieVehicule, VersionLexique versionLexique, Set<Lexique> childs, Lexique parent,
                   List<Inspection> inspections, Boolean haschild) {
        super();
        this.id = id;
        this.libelle = libelle;
        this.code = code;
        Visuel = visuel;

        this.categorieVehicule = categorieVehicule;
        this.versionLexique = versionLexique;
        this.childs = childs;
        this.parent = parent;
        this.inspections = inspections;
        this.haschild = haschild;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean getVisuel() {
        return Visuel;
    }

    public void setVisuel(Boolean visuel) {
        Visuel = visuel;
    }

    public VersionLexique getVersionLexique() {
        return versionLexique;
    }

    public void setVersionLexique(VersionLexique versionLexique) {
        this.versionLexique = versionLexique;
    }

    public Set<Lexique> getChilds() {
        return childs;
    }

    public void setChilds(Set<Lexique> childs) {
        this.childs = childs;
    }

    public Lexique getParent() {
        return parent;
    }

    public void setParent(Lexique parent) {
        this.parent = parent;
    }


    public Set<Client> getClients() {
        return clients;
    }

    public void setClients(Set<Client> clients) {
        this.clients = clients;
    }

    public CategorieVehicule getCategorieVehicule() {
        return categorieVehicule;
    }

    public void setCategorieVehicule(CategorieVehicule categorieVehicule) {
        this.categorieVehicule = categorieVehicule;
    }


    public Boolean getHaschild() {
        return haschild;
    }


    public void setHaschild(Boolean haschild) {
        this.haschild = haschild;
    }


    public void setHaschild(boolean haschild) {
        this.haschild = haschild;
    }


    public List<Inspection> getInspections() {
        return inspections;
    }


    public void setInspections(List<Inspection> inspections) {
        this.inspections = inspections;
    }

    public Set<Seuil> getSeuils() {
        return seuils;
    }

    public void setSeuils(Set<Seuil> seuils) {
        this.seuils = seuils;
    }

    public Classification getClassification() {
        return classification;
    }

    public void setClassification(Classification classification) {
        this.classification = classification;
    }


}