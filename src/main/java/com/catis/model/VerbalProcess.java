package com.catis.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.model.configuration.JournalData;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class VerbalProcess extends JournalData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String reference;

    private String signature; // qrc

    private boolean status;

    @OneToOne

    private Visite visite;

    @OneToMany(mappedBy = "verbalProcess")
    @JsonIgnore
    private Set<RapportDeVisite> rapportDeVisites;

    public VerbalProcess() {

        // TODO Auto-generated constructor stub
    }


    public boolean isStatus() {
        return status;
    }


    public void setStatus(boolean status) {
        this.status = status;
    }


    public VerbalProcess(Long id, String reference, String signature, boolean status, Visite visite,
                         Set<RapportDeVisite> rapportDeVisites) {
        super();
        this.id = id;
        this.reference = reference;
        this.signature = signature;
        this.status = status;
        this.visite = visite;
        this.rapportDeVisites = rapportDeVisites;
    }


    public Set<RapportDeVisite> getRapportDeVisites() {
        return rapportDeVisites;
    }


    public void setRapportDeVisites(Set<RapportDeVisite> rapportDeVisites) {
        this.rapportDeVisites = rapportDeVisites;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public Visite getVisite() {
        return visite;
    }

    public void setVisite(Visite visite) {
        this.visite = visite;
    }

}