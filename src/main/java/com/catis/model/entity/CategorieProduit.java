package com.catis.model.entity;

import java.util.Objects;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.model.configuration.JournalData;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "t_categorieproduit")
@Audited
@SQLDelete(sql = "UPDATE t_categorieproduit SET active_status=false WHERE categorie_produit_id=?")
public class CategorieProduit extends JournalData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categorieProduitId;
    private String libelle;
    private String description;

    @OneToMany(mappedBy = "categorieProduit")
    @JsonIgnore
    Set<Produit> produits;

    public CategorieProduit() {
        // TODO Auto-generated constructor stub
    }

    public CategorieProduit(Long categorieProduitId, String libelle, String description, Set<Produit> produits) {
        this.categorieProduitId = categorieProduitId;
        this.libelle = libelle;
        this.description = description;
        this.produits = produits;
    }

    public Long getCategorieProduitId() {
        return categorieProduitId;
    }

    public void setCategorieProduitId(Long categorieProduitId) {
        this.categorieProduitId = categorieProduitId;
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

    public Set<Produit> getProduits() {
        return produits;
    }

    public void setProduits(Set<Produit> produits) {
        this.produits = produits;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CategorieProduit)) return false;
        CategorieProduit that = (CategorieProduit) o;
        return Objects.equals(getCategorieProduitId(), that.getCategorieProduitId());
    }

}
