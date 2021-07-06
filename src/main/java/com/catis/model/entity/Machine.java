package com.catis.model.entity;

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

import org.hibernate.annotations.SQLDelete;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.model.configuration.JournalData;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "t_machine")
@EntityListeners(AuditingEntityListener.class)
@Audited
@SQLDelete(sql = "UPDATE t_machine SET active_status=false WHERE id_machine=?")
public class Machine extends JournalData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMachine;
    private String numSerie; // numéro de série
    private String fabriquant;
    private String model;



    @OneToMany(fetch = FetchType.LAZY, mappedBy = "machine")
    @JsonIgnore
    private Set<LigneMachine> ligneMachines;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "machine")
    @JsonIgnore
    private Set<CategorieTestMachine> categorieTestMachine;

    @OneToMany(mappedBy = "machine")
    private Set<GieglanFile> gieglanFiles;

    @ManyToOne
    private ConstructorModel constructorModel;

    public Machine() {

        // TODO Auto-generated constructor stub
    }

    public Machine(Long idMachine, String numSerie, String fabriquant, String model,  Set<LigneMachine> ligneMachines, Set<CategorieTestMachine> categorieTestMachine, Set<GieglanFile> gieglanFiles, ConstructorModel constructorModel) {
        this.idMachine = idMachine;
        this.numSerie = numSerie;
        this.fabriquant = fabriquant;
        this.model = model;

        this.ligneMachines = ligneMachines;
        this.categorieTestMachine = categorieTestMachine;
        this.gieglanFiles = gieglanFiles;
        this.constructorModel = constructorModel;
    }

    public ConstructorModel getConstructorModel() {
        return constructorModel;
    }

    public void setConstructorModel(ConstructorModel constructorModel) {
        this.constructorModel = constructorModel;
    }

    public Long getIdMachine() {
        return idMachine;
    }

    public void setIdMachine(Long idMachine) {
        this.idMachine = idMachine;
    }

    public String getNumSerie() {
        return numSerie;
    }

    public void setNumSerie(String numSerie) {
        this.numSerie = numSerie;
    }

    public String getFabriquant() {
        return fabriquant;
    }

    public void setFabriquant(String fabriquant) {
        this.fabriquant = fabriquant;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }



    public Set<LigneMachine> getLigneMachines() {
        return ligneMachines;
    }

    public void setLigneMachines(Set<LigneMachine> ligneMachines) {
        this.ligneMachines = ligneMachines;
    }

    public Set<CategorieTestMachine> getCategorieTestMachine() {
        return categorieTestMachine;
    }

    public void setCategorieTestMachine(Set<CategorieTestMachine> categorieTestMachine) {
        this.categorieTestMachine = categorieTestMachine;
    }

    public Set<GieglanFile> getGieglanFiles() {
        return gieglanFiles;
    }

    public void setGieglanFiles(Set<GieglanFile> gieglanFiles) {
        this.gieglanFiles = gieglanFiles;
    }
}
