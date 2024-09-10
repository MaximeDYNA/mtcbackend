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
@EntityListeners(AuditingEntityListener.class)
@Table(name = "t_ligne")
@Audited
@SQLDelete(sql = "UPDATE t_ligne SET active_status=false WHERE id=?")
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class Ligne extends JournalData {

    @Id
    @GeneratedValue(generator = "UUID")
    @Type(type="uuid-char")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID idLigne;
    private String description;
    private String nom;

    @ManyToOne
    private CategorieVehicule categorieVehicule;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "ligne")
    @JsonIgnore
    private Set<LigneMachine> ligneMachines;

    @OneToMany(mappedBy = "ligne",fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Inspection> inspections;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ligne)) return false;
        Ligne ligne = (Ligne) o;
        return Objects.equals(getIdLigne(), ligne.getIdLigne());
    }


}
