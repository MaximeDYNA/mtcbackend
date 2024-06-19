package com.catis.model.entity;

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

import java.util.Objects;
import java.util.UUID;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "t_caisse")
@Audited
@SQLDelete(sql = "UPDATE t_caisse SET active_status=false WHERE caisse_id=?")
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class Caisse extends JournalData {

    @Id
    @GeneratedValue(generator = "UUID")
    @Type(type="uuid-char")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID caisseId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String libelle;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String description;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "caisse")
    @JsonIgnore
    private Caissier caissier;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Caisse)) return false;
        Caisse caisse = (Caisse) o;
        return Objects.equals(caisseId, caisse.caisseId);
    }

}
