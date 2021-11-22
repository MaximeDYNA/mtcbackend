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
@Table(name = "t_proprietairevehicule")
@Audited
@SQLDelete(sql = "UPDATE t_proprietairevehicule SET active_status=false WHERE proprietaire_vehicule_id=?")
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class ProprietaireVehicule extends JournalData {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID proprietaireVehiculeId;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE })
    private Partenaire partenaire;

    @OneToMany(mappedBy = "proprietaireVehicule")
    @JsonIgnore
    private Set<CarteGrise> cartegrises;

    private double score;

    private String description;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProprietaireVehicule)) return false;
        ProprietaireVehicule that = (ProprietaireVehicule) o;
        return Objects.equals(getProprietaireVehiculeId(), that.getProprietaireVehiculeId());
    }

}
