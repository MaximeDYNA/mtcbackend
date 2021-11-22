package com.catis.model.entity;

import java.util.Set;
import java.util.UUID;

import javax.persistence.*;

import com.catis.model.control.GieglanFile;
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
@Table(name = "t_machine")
@EntityListeners(AuditingEntityListener.class)
@Audited
@SQLDelete(sql = "UPDATE t_machine SET active_status=false WHERE id_machine=?")
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class Machine extends JournalData {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID idMachine;
    private String numSerie; // numéro de série
    private String fabriquant;
    private String model;



    @OneToMany(fetch = FetchType.LAZY, mappedBy = "machine")
    @JsonIgnore
    private Set<LigneMachine> ligneMachines;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "machine")
    @JsonIgnore
    private Set<CategorieTestMachine> categorieTestMachine;

    @OneToMany(mappedBy = "machine")
    private Set<GieglanFile> gieglanFiles;

    @ManyToOne
    private ConstructorModel constructorModel;

}
