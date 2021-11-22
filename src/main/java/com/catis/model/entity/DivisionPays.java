package com.catis.model.entity;

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

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "t_divisionpays")
@Audited
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class DivisionPays extends JournalData {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID divisionPaysId;

    @ManyToOne
    private Pays pays;
    @OneToMany(mappedBy = "divisionPays")
    Set<Adresse> adresses;
    private String libelle;
    private String description;

    @OneToMany(mappedBy = "parent")
    private Set<DivisionPays> childs;
    @ManyToOne
    private DivisionPays parent;


}
