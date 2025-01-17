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
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.model.configuration.JournalData;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "t_caissier")
@Audited
@SQLDelete(sql = "UPDATE t_caissier SET active_status=false WHERE id=?")
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class Caissier extends JournalData {

    @Id
    @GeneratedValue(generator = "UUID")
    @Type(type="uuid-char")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID caissierId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String codeCaissier;

    private String nom;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.PERSIST,fetch = FetchType.LAZY)
    private Partenaire partenaire;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnore
    @OneToOne (cascade = CascadeType.PERSIST,fetch = FetchType.LAZY)
    private Caisse caisse;

    @ManyToOne(optional = true,  cascade = CascadeType.PERSIST,fetch = FetchType.LAZY) 
    @JsonIgnore// id utilisateur optionel
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
