package com.catis.model.entity;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.model.configuration.JournalData;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "t_divisionpays")
@Audited
public class DivisionPays extends JournalData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long divisionPaysId;

    @ManyToOne
    private Pays pays;
    @OneToMany(mappedBy = "divisionPays")
    Set<Adresse> adresses;
    private String libelle;
    private String description;

    @OneToMany(mappedBy = "parent")
    private Set<DivisionPays> childs;
    @ManyToOne
    private DivisionPays parent;

    public DivisionPays() {

    }

    public DivisionPays(Long divisionPaysId, Pays pays, Set<Adresse> adresses, String libelle, String description,
                        Set<DivisionPays> childs, DivisionPays parent) {

        this.divisionPaysId = divisionPaysId;
        this.pays = pays;
        this.adresses = adresses;
        this.libelle = libelle;
        this.description = description;
        this.childs = childs;
        this.parent = parent;
    }

    public Long getDivisionPaysId() {
        return divisionPaysId;
    }

    public void setDivisionPaysId(Long divisionPaysId) {
        this.divisionPaysId = divisionPaysId;
    }

    public Pays getPays() {
        return pays;
    }

    public void setPays(Pays pays) {
        this.pays = pays;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Adresse> getAdresses() {
        return adresses;
    }

    public void setAdresses(Set<Adresse> adresses) {
        this.adresses = adresses;
    }

    public Set<DivisionPays> getChilds() {
        return childs;
    }

    public void setChilds(Set<DivisionPays> childs) {
        this.childs = childs;
    }

    public DivisionPays getParent() {
        return parent;
    }

    public void setParent(DivisionPays parent) {
        this.parent = parent;
    }

}