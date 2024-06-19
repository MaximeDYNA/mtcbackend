package com.catis.model.entity;

import java.util.Date;
import java.util.List;
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
@Audited
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class VersionLexique extends JournalData {

    @Id
    @GeneratedValue(generator = "UUID")
    @Type(type="uuid-char")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    private String libelle;

    private Date date;

    private String version;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "versionLexique",fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Lexique> lexiques;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "visite",fetch = FetchType.LAZY)
    @JsonIgnore
    private List<RapportDeVisite> rapportDeVisites;


}