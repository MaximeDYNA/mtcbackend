package com.catis.model.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.model.configuration.JournalData;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "t_produit")
@EntityListeners(AuditingEntityListener.class)
@Audited
@SQLDelete(sql = "UPDATE t_produit SET active_status=false WHERE produit_id=?")
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class Produit extends JournalData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long produitId;

    private String libelle;

    private String description;

    private double prix;

    private int delaiValidite;

    private String img;

    @OneToMany(mappedBy = "produit")
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

    @OneToMany(mappedBy = "produit")
    @JsonIgnore
    private Set<Posales> posales;

    @ManyToOne
    private CategorieProduit categorieProduit;

    @ManyToOne
    private CategorieVehicule categorieVehicule;

    @ManyToMany
    @JsonIgnore
    private Set<Seuil> seuils = new HashSet<>();



}
