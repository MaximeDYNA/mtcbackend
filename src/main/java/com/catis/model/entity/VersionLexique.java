package com.catis.model.entity;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.model.configuration.JournalData;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Audited
public class VersionLexique extends JournalData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String libelle;

    private Date date;

    private String version;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "versionLexique")
    @JsonIgnore
    private Set<Lexique> lexiques;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "visite")
    @JsonIgnore
    private List<RapportDeVisite> rapportDeVisites;



    public VersionLexique() {

        // TODO Auto-generated constructor stub
    }

    public VersionLexique(Long id, String libelle, Date date, Set<Lexique> lexiques,
                          List<RapportDeVisite> rapportDeVisites) {

        this.id = id;
        this.libelle = libelle;
        this.date = date;
        this.lexiques = lexiques;
        this.rapportDeVisites = rapportDeVisites;

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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Set<Lexique> getLexiques() {
        return lexiques;
    }

    public void setLexiques(Set<Lexique> lexiques) {
        this.lexiques = lexiques;
    }

    public List<RapportDeVisite> getRapportDeVisites() {
        return rapportDeVisites;
    }

    public void setRapportDeVisites(List<RapportDeVisite> rapportDeVisites) {
        this.rapportDeVisites = rapportDeVisites;
    }


    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

}