package com.catis.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.model.configuration.JournalData;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "t_proprietairevehicule")
public class ProprietaireVehicule extends JournalData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long proprietaireVehiculeId;



    @ManyToOne
    private Partenaire partenaire;

    @OneToMany(mappedBy = "proprietaireVehicule")
    @JsonIgnore
    private Set<CarteGrise> cartegrises;

    private String description;

    public ProprietaireVehicule() {

    }

    public ProprietaireVehicule(Long proprietaireVehiculeId, Partenaire partenaire,
                                Set<CarteGrise> cartegrises, String description) {

        this.proprietaireVehiculeId = proprietaireVehiculeId;

        this.partenaire = partenaire;
        this.cartegrises = cartegrises;
        this.description = description;
    }

    public Long getProprietaireVehiculeId() {
        return proprietaireVehiculeId;
    }

    public void setProprietaireVehiculeId(Long proprietaireVehiculeId) {
        this.proprietaireVehiculeId = proprietaireVehiculeId;
    }

    public Partenaire getPartenaire() {
        return partenaire;
    }

    public void setPartenaire(Partenaire partenaire) {
        this.partenaire = partenaire;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<CarteGrise> getCartegrises() {
        return cartegrises;
    }

    public void setCartegrises(Set<CarteGrise> cartegrises) {
        this.cartegrises = cartegrises;
    }



}
