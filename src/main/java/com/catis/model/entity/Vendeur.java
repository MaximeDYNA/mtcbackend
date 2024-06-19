package com.catis.model.entity;

import java.util.Set;
import java.util.UUID;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.model.configuration.JournalData;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "t_vendeur")
@EntityListeners(AuditingEntityListener.class)
@Audited
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class Vendeur extends JournalData {
    // entité capable d'avoir des commisions sur une vente
    @Id
    @GeneratedValue(generator = "UUID")
    @Type(type="uuid-char")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID vendeurId;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    private Partenaire partenaire;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "vendeur")
    @JsonIgnore
    private Set<Vente> ventes;



}
