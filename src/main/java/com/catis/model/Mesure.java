package com.catis.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.model.configuration.JournalData;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "t_mesure")
@EntityListeners(AuditingEntityListener.class)
public class Mesure extends JournalData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMesure;
    private String code;
    private String description;

    @ManyToOne
    private Organisation organisation;

    @ManyToOne
    private Formule formule;

    @ManyToMany(mappedBy = "mesures")
    private Set<CategorieTestVehicule> categorieTestVehicules;

    @OneToMany(mappedBy = "mesure")
    private List<ValeurTest> valeurTests = new ArrayList<>();

    public Mesure() {

        // TODO Auto-generated constructor stub
    }

    public Long getIdMesure() {
        return idMesure;
    }

    public void setIdMesure(Long idMesure) {
        this.idMesure = idMesure;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Organisation getOganisation() {
        return organisation;
    }

    public void setOganisation(Organisation organisation) {
        this.organisation = organisation;
    }

    public Formule getFormule() {
        return formule;
    }

    public void setFormule(Formule formule) {
        this.formule = formule;
    }

    public Organisation getOrganisation() {
        return organisation;
    }

    public void setOrganisation(Organisation organisation) {
        this.organisation = organisation;
    }

    public Set<CategorieTestVehicule> getCategorieTestVehicules() {
        return categorieTestVehicules;
    }

    public void setCategorieTestVehicules(Set<CategorieTestVehicule> categorieTestVehicules) {
        this.categorieTestVehicules = categorieTestVehicules;
    }

    public Mesure(Long idMesure, String code, String description, Organisation organisation,
                  Formule formule, Set<CategorieTestVehicule> categorieTestVehicules) {
        super();
        this.idMesure = idMesure;
        this.code = code;
        this.description = description;
        this.organisation = organisation;
        this.formule = formule;
        this.categorieTestVehicules = categorieTestVehicules;
    }

}
