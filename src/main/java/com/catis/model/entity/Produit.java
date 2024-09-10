package com.catis.model.entity;


import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.model.configuration.JournalData;
import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table(name = "t_produit")
@EntityListeners(AuditingEntityListener.class)
@Audited
@SQLDelete(sql = "UPDATE t_produit SET active_status=false WHERE id=?")
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class Produit extends JournalData {

    @Id
    @GeneratedValue(generator = "UUID")
    @Type(type="uuid-char")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID produitId;

    private String libelle;

    private String description;

    private double prix;

    private int delaiValidite;

    private String img;

    @OneToMany(mappedBy = "produit",fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<CategorieTestProduit> categorieTestProduits = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "produit")
    @JsonIgnore
    private Set<DetailVente> detailVente;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "produit")
    @JsonIgnore
    private Set<CarteGrise> carteGrise;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "produit")
    @JsonIgnore
    private Set<TaxeProduit> taxeProduit;

    @OneToMany(mappedBy = "produit",fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Posales> posales;

    @ManyToOne
    private CategorieProduit categorieProduit;

    @ManyToOne(fetch = FetchType.LAZY)
    private CategorieVehicule categorieVehicule;

    @ManyToMany(fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Seuil> seuils = new HashSet<>();


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Produit)) return false;
        Produit produit = (Produit) o;
        return Objects.equals(getProduitId(), produit.getProduitId());
    }

}
