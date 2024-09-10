
package com.catis.model.entity;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


import com.catis.model.configuration.JournalData;
import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "t_vente")
@Audited
@AllArgsConstructor @NoArgsConstructor
@Setter @Getter
public class Vente extends JournalData {
    @Id
    @GeneratedValue(generator = "UUID")
    @Type(type="uuid-char")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID idVente;
    private double montantTotal;
    private double montantHT;
    private int statut;

   
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE })
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    private Vendeur vendeur;

    @ManyToOne (cascade = {CascadeType.PERSIST, CascadeType.MERGE },fetch = FetchType.LAZY)
    private Contact contact;

    @OneToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Visite visite;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private SessionCaisse sessionCaisse;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "vente")
    @JsonIgnore
    private Set<OperationCaisse> operationCaisse;

    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "vente", cascade = CascadeType.ALL)
    private Set<DetailVente> detailventes;

    private String numFacture;

    public Vente(UUID idVente, double montantTotal, double montantHT, Client client, Vendeur vendeur,
                 @NotEmpty @NotNull Contact contact, SessionCaisse sessionCaisse, Set<OperationCaisse> operationCaisse,
                 Set<DetailVente> detailventes, String numFacture) {

        this.idVente = idVente;
        this.montantTotal = montantTotal;
        this.montantHT = montantHT;
        this.client = client;
        this.vendeur = vendeur;
        this.contact = contact;
        this.sessionCaisse = sessionCaisse;
        this.operationCaisse = operationCaisse;
        this.detailventes = detailventes;
        this.numFacture = numFacture;
    }


    public String getLibelleStatut() {
        if (this.statut == 0)
            return "payé";
        else if (this.statut == 1) {
            return "partiellement payé";
        } else if (this.statut == 2) {
            return "impayé";
        } else
            return "statut erroné";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vente)) return false;
        Vente vente = (Vente) o;
        return Objects.equals(getIdVente(), vente.getIdVente());
    }

}
