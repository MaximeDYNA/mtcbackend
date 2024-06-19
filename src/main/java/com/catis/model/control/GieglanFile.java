package com.catis.model.control;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

import javax.persistence.*;

import com.catis.model.entity.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.model.configuration.JournalData;

/**
 * @author AubryYvan
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Audited
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class GieglanFile extends JournalData {

    @Id
    @GeneratedValue(generator = "UUID")
    @Type(type="uuid-char")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    private String name;

    private Date fileCreatedAt;


    private Boolean isAccept;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(255) default 'MEASURE'")
    private FileType type;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(255) default 'INITIALIZED'")
    private StatusType status;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonIgnore
    private Inspection inspection;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonIgnore
    private Machine machine;

    @OneToMany(mappedBy = "gieglanFile", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<ValeurTest> valeurTests;

    @OneToMany(mappedBy = "gieglanFile",fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<RapportDeVisite> rapportDeVisites;

    @OneToOne(mappedBy = "gieglanFile", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private MesureVisuel mesureVisuel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private CategorieTest categorieTest;


    public enum FileType {
        MEASURE, MACHINE, CARD_REGISTRATION
    }

    public enum StatusType {
        INITIALIZED, REJECTED, VALIDATED, NOT_DEFINED
    }


}