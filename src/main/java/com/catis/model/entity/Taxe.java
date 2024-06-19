package com.catis.model.entity;

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
@Table(name = "t_taxe")
@EntityListeners(AuditingEntityListener.class)
@Audited
@SQLDelete(sql = "UPDATE t_taxe SET active_status=false WHERE taxe_id=?")
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class Taxe extends JournalData {
    @Id
    @GeneratedValue(generator = "UUID")
    @Type(type="uuid-char")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID taxeId;

    private String nom;
    private String description;
    private double valeur;

    private boolean incluse;

    @OneToMany(mappedBy = "taxe", cascade = CascadeType.PERSIST,fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<TaxeProduit> taxeProduit;

}
