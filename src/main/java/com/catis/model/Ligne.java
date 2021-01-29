package com.catis.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
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
@Table(name = "t_ligne")
public class Ligne extends JournalData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idLigne;
    private String description;

    @ManyToOne
    private Organisation organisation;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "ligne")
    @JsonIgnore
    private Set<LigneMachine> ligneMachines;

    @OneToMany(mappedBy = "ligne")
    @JsonIgnore
    private Set<Inspection> inspections;

    public Ligne() {

        // TODO Auto-generated constructor stub
    }

    public Ligne(Long idLigne, String description, Organisation organisation, Set<LigneMachine> ligneMachines,
                 Set<Inspection> inspections) {

        this.idLigne = idLigne;
        this.description = description;
        this.organisation = organisation;
        this.ligneMachines = ligneMachines;
        this.inspections = inspections;
    }

    public Long getIdLigne() {
        return idLigne;
    }

    public void setIdLigne(Long idLigne) {
        this.idLigne = idLigne;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Organisation getOrganisation() {
        return organisation;
    }

    public void setOrganisation(Organisation organisation) {
        this.organisation = organisation;
    }

    public Set<LigneMachine> getLigneMachines() {
        return ligneMachines;
    }

    public void setLigneMachines(Set<LigneMachine> ligneMachines) {
        this.ligneMachines = ligneMachines;
    }

    public Set<Inspection> getInspections() {
        return inspections;
    }

    public void setInspections(Set<Inspection> inspections) {
        this.inspections = inspections;
    }

}
