package com.catis.model.entity;

import java.util.Objects;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.catis.model.control.GieglanFile;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.model.configuration.JournalData;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "t_categorietest")
@Audited
public class CategorieTest extends JournalData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCategorieTest;

    private String libelle;

    private String description;

    private String icon;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "categorieTest")
    @JsonIgnore
    private Set<CategorieTestMachine> categorieTestMachines;

    @OneToMany(mappedBy = "categorieTest")
    @JsonIgnore
    private Set<CategorieTestProduit> categorieTestVehicules;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "categorieTest")
    @JsonIgnore
    private Set<GieglanFile> gieglanFiles;

    public Long getIdCategorieTest() {
        return idCategorieTest;
    }

    public void setIdCategorieTest(Long idCategorieTest) {
        this.idCategorieTest = idCategorieTest;
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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<CategorieTestMachine> getCategorieTestMachines() {
        return categorieTestMachines;
    }

    public void setCategorieTestMachines(Set<CategorieTestMachine> categorieTestMachines) {
        this.categorieTestMachines = categorieTestMachines;
    }

    public Set<CategorieTestProduit> getCategorieTestVehicules() {
        return categorieTestVehicules;
    }

    public void setCategorieTestVehicules(Set<CategorieTestProduit> categorieTestVehicules) {
        this.categorieTestVehicules = categorieTestVehicules;
    }


    public Set<GieglanFile> getGieglanFiles() {
        return gieglanFiles;
    }

    public void setGieglanFiles(Set<GieglanFile> gieglanFiles) {
        this.gieglanFiles = gieglanFiles;
    }

    public CategorieTest(Long idCategorieTest, String libelle, String description,
                         Set<CategorieTestMachine> categorieTestMachines, Set<CategorieTestProduit> categorieTestVehicules,
                         Set<GieglanFile> gieglanFiles) {
        super();
        this.idCategorieTest = idCategorieTest;
        this.libelle = libelle;
        this.description = description;
        this.categorieTestMachines = categorieTestMachines;
        this.categorieTestVehicules = categorieTestVehicules;
        this.gieglanFiles = gieglanFiles;
    }

    public CategorieTest() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CategorieTest)) return false;
        CategorieTest that = (CategorieTest) o;
        return Objects.equals(getIdCategorieTest(), that.getIdCategorieTest());
    }

}
