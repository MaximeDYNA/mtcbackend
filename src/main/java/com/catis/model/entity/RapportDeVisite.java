package com.catis.model.entity;

import javax.persistence.*;

import com.catis.model.control.GieglanFile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.model.configuration.JournalData;

import java.util.UUID;

/**
 * @author AubryYvan
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Audited
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class RapportDeVisite extends JournalData {

    @Id
    @GeneratedValue(generator = "UUID")
    @Type(type="uuid-char")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    private String result;

    private boolean decision;

    private String codeMessage;

    @ManyToOne(cascade = CascadeType.ALL)
    private Visite visite;

    @ManyToOne(cascade = CascadeType.ALL)
    private Seuil seuil;

    @ManyToOne
    private GieglanFile gieglanFile;


    @ManyToOne
    private VerbalProcess verbalProcess;


}