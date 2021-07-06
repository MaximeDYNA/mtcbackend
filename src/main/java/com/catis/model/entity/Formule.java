package com.catis.model.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.model.configuration.JournalData;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Audited
public class Formule extends JournalData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    @OneToMany(mappedBy = "formule")
    @JsonIgnore
    private Set<Mesure> mesures;

    @OneToMany(mappedBy = "formule", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Seuil> seuils;

    public Formule() {
        // TODO Auto-generated constructor stub
    }

    public Formule(Long id, String description, Set<Mesure> mesures, Set<Seuil> seuils) {
        this.id = id;
        this.description = description;
        this.mesures = mesures;
        this.seuils = seuils;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Mesure> getMesures() {
        return mesures;
    }

    public void setMesures(Set<Mesure> mesures) {
        this.mesures = mesures;
    }

    public Set<Seuil> getSeuils() {
        return seuils;
    }

    public void setSeuils(Set<Seuil> seuils) {
        this.seuils = seuils;
    }

}