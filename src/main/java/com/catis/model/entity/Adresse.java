package com.catis.model.entity;

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

import java.util.UUID;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "t_adresse")
@Audited
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
public class Adresse extends JournalData  {

    @Id
    @GeneratedValue(generator = "UUID")
    @Type(type="uuid-char")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID adresseId;
    private String nom;
    private String description;


    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Partenaire partenaire;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Pays pays;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private DivisionPays divisionPays;


}
