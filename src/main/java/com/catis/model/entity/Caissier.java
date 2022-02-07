package com.catis.model.entity;

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
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.model.configuration.JournalData;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "t_caissier")
@Audited
@SQLDelete(sql = "UPDATE t_caissier SET active_status=false WHERE caissier_id=?")
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class Caissier extends JournalData {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID caissierId;

    private String codeCaissier;

    private String nom;


    @ManyToOne(cascade = CascadeType.PERSIST)
    private Partenaire partenaire;

    @OneToOne (cascade = CascadeType.PERSIST)
    private Caisse caisse;

    @ManyToOne(optional = true,  cascade = CascadeType.PERSIST) // id utilisateur optionel
    private Utilisateur user;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "caissier")
    @JsonIgnore
    private Set<SessionCaisse> sessionCaisses;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "caissier")
    @JsonIgnore
    private Set<Visite> visites;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Caissier)) return false;
        Caissier caissier = (Caissier) o;
        return Objects.equals(getCaissierId(), caissier.getCaissierId());
    }


}
