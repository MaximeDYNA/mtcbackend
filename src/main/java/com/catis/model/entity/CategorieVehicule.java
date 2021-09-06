package com.catis.model.entity;

import java.util.Objects;
import java.util.Set;

import javax.persistence.*;

import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.model.configuration.JournalData;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Audited
public class CategorieVehicule extends JournalData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;

    @OneToMany(mappedBy = "categorieVehicule")
    @JsonIgnore
    private Set<Ligne> lignes;

    @OneToMany(mappedBy = "categorieVehicule")
    @JsonIgnore
    private Set<Lexique> lexique;

    @OneToMany(mappedBy = "categorieVehicule")
    @JsonIgnore
    private Set<Produit> produits;

    public CategorieVehicule() {
        // TODO Auto-generated constructor stub
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public Set<Produit> getProduits() {
        return produits;
    }


    public CategorieVehicule(Long id, String type, Set<CategorieTestProduit> categorieTestVehicules,
                             Set<Lexique> lexique, Set<Produit> produits) {
        super();
        this.id = id;
        this.type = type;
        this.lexique = lexique;
        this.produits = produits;
    }


    public void setProduits(Set<Produit> produits) {
        this.produits = produits;
    }


    public Set<Lexique> getLexique() {
        return lexique;
    }

    public void setLexique(Set<Lexique> lexique) {
        this.lexique = lexique;
    }


    public Set<Ligne> getLignes() {
        return lignes;
    }

    public void setLignes(Set<Ligne> lignes) {
        this.lignes = lignes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CategorieVehicule)) return false;
        CategorieVehicule that = (CategorieVehicule) o;
        return Objects.equals(getId(), that.getId());
    }

}