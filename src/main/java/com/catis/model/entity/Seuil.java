package com.catis.model.entity;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.model.configuration.JournalData;

/**
 * @author AubryYvan
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Audited
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class Seuil extends JournalData {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    private double value;

    private String operande;

    private String codeMessage;

    private boolean decision;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "seuil",fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<RapportDeVisite> rapportDeVisites;

    @ManyToOne(fetch = FetchType.LAZY)
    private Lexique lexique;

    @ManyToOne(fetch = FetchType.LAZY)
    private Formule formule;

    @ManyToMany(mappedBy = "seuils",fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Produit> produits = new HashSet<>();


}