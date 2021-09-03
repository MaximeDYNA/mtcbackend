package com.catis.model.entity;

import java.util.Objects;
import java.util.Set;

import javax.persistence.*;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.model.configuration.JournalData;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "t_proprietairevehicule")
@Audited
@SQLDelete(sql = "UPDATE t_proprietairevehicule SET active_status=false WHERE proprietaire_vehicule_id=?")
public class ProprietaireVehicule extends JournalData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long proprietaireVehiculeId;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE })
    private Partenaire partenaire;

    @OneToMany(mappedBy = "proprietaireVehicule")
    @JsonIgnore
    private Set<CarteGrise> cartegrises;

    private double score;

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

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProprietaireVehicule)) return false;
        ProprietaireVehicule that = (ProprietaireVehicule) o;
        return Objects.equals(getProprietaireVehiculeId(), that.getProprietaireVehiculeId());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getProprietaireVehiculeId());
    }
}
