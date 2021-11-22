package com.catis.model.entity;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.model.configuration.JournalData;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "t_sessioncaisse")
@EntityListeners(AuditingEntityListener.class)
@Audited
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class SessionCaisse extends JournalData {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID sessionCaisseId;

    private Date dateHeureOuverture;

    private Date dateHeureFermeture;


    @ManyToOne
    private Caissier caissier;

    private double montantOuverture;

    private double montantfermeture;

    private boolean active;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "sessionCaisse")
    @JsonIgnore
    Set<OperationCaisse> operationCaisse;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "sessionCaisse")
    @JsonIgnore
    private Set<Vente> vente;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "sessionCaisse")
    @JsonIgnore
    Set<Hold> holds;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "sessionCaisse")
    @JsonIgnore
    private Set<Posales> posales;


}
