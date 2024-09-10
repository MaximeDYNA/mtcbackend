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

@Entity
@Table(name = "t_controleur")
@EntityListeners(AuditingEntityListener.class)
@Audited
@SQLDelete(sql = "UPDATE t_controleur SET active_status=false WHERE id=?")
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class Controleur extends JournalData {
    @Id
    @GeneratedValue(generator = "UUID")
    @Type(type="uuid-char")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID idControleur;
    private String agremment;
    private double score=0;

    @OneToOne(optional = true, fetch = FetchType.LAZY) // id utilisateur optionel
    @JsonIgnore
    private Utilisateur utilisateur;

    @OneToOne (cascade = {CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.LAZY)
    @JsonIgnore
    private Partenaire partenaire;


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "controleur")
    @JsonIgnore
    private Set<Inspection> inspections;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Controleur)) return false;
        Controleur that = (Controleur) o;
        return Objects.equals(getIdControleur(), that.getIdControleur());
    }

}
