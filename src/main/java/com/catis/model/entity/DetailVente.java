package com.catis.model.entity;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.model.configuration.JournalData;

import java.util.Objects;
import java.util.UUID;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "t_detailvente")
@Audited
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class DetailVente extends JournalData {
    // table pivot entre produit et vente
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID idDetailVente;
    private String reference;
    private double prix;

    @ManyToOne
    private Produit produit;

    @ManyToOne
    @JsonIgnore
    private Vente vente;



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DetailVente)) return false;
        DetailVente that = (DetailVente) o;
        return getIdDetailVente() == that.getIdDetailVente();
    }


}
