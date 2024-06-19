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
@EntityListeners(AuditingEntityListener.class)
@Table(name = "t_divisionpays")
@Audited
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class DivisionPays extends JournalData {

    @Id
    @GeneratedValue(generator = "UUID")
    @Type(type="uuid-char")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID divisionPaysId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Pays pays;

    @OneToMany(mappedBy = "divisionPays", fetch = FetchType.LAZY)
    @JsonIgnore
    Set<Adresse> adresses;

    private String libelle;
    private String description;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<DivisionPays> childs;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private DivisionPays parent;


}
