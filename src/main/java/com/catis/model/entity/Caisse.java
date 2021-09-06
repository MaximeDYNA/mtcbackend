package com.catis.model.entity;

import javax.persistence.*;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.model.configuration.JournalData;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Objects;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "t_caisse")
@Audited
@SQLDelete(sql = "UPDATE t_caisse SET active_status=false WHERE caisse_id=?")
public class Caisse extends JournalData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long caisseId;

    private String libelle;

    private String description;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "caisse")
    @JsonIgnore
    private Caissier caissier;

    public Long getCaisse_id() {
        return caisseId;
    }

    public void setCaisse_id(Long caisse_id) {
        this.caisseId = caisse_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public Caisse() {

        // TODO Auto-generated constructor stub
    }

    public Caisse(Long caisse_id, String libelle, String description, Caissier caissier) {
        this.caisseId = caisse_id;
        this.libelle = libelle;
        this.description = description;
        this.caissier = caissier;
    }

    public Caissier getCaissier() {
        return caissier;
    }

    public void setCaissier(Caissier caissier) {
        this.caissier = caissier;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Caisse)) return false;
        Caisse caisse = (Caisse) o;
        return Objects.equals(caisseId, caisse.caisseId);
    }

}
