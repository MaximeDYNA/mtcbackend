package com.catis.model.entity;

import java.util.Set;

import javax.persistence.*;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.model.configuration.JournalData;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "t_ligne")
@Audited
@SQLDelete(sql = "UPDATE t_ligne SET active_status=false WHERE id_ligne=?")
public class Ligne extends JournalData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idLigne;
    private String description;
    private String nom;

    @ManyToOne
    private CategorieVehicule categorieVehicule;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "ligne")
    @JsonIgnore
    private Set<LigneMachine> ligneMachines;

    @OneToMany(mappedBy = "ligne")
    @JsonIgnore
    private Set<Inspection> inspections;

    public Ligne() {

        // TODO Auto-generated constructor stub
    }

    public Ligne(Long idLigne, String description,  Set<LigneMachine> ligneMachines,
                 Set<Inspection> inspections) {

        this.idLigne = idLigne;
        this.description = description;

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

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public CategorieVehicule getCategorieVehicule() {
        return categorieVehicule;
    }

    public void setCategorieVehicule(CategorieVehicule categorieVehicule) {
        this.categorieVehicule = categorieVehicule;
    }
}
