package com.catis.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.model.configuration.JournalData;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class CategorieVehicule extends JournalData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;

    @OneToMany(mappedBy = "categorieVehicule")
    @JsonIgnore
    private Set<CategorieTestVehicule> categorieTestVehicules;

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


    public CategorieVehicule(Long id, String type, Set<CategorieTestVehicule> categorieTestVehicules,
                             Set<Lexique> lexique, Set<Produit> produits) {
        super();
        this.id = id;
        this.type = type;
        this.categorieTestVehicules = categorieTestVehicules;
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

}