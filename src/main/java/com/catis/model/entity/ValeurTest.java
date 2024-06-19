package com.catis.model.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.catis.model.control.GieglanFile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.model.control.GieglanFile.StatusType;
import com.catis.model.configuration.JournalData;

import java.util.UUID;

@Entity
@Table(name = "t_valeurtest")
@Audited
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ValeurTest extends JournalData {

    @Id
    @GeneratedValue(generator = "UUID")
    @Type(type="uuid-char")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID idValeurTest;

    private String code;

    private String valeur;

    private Integer crc;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(255) default 'INITIALIZED'")
    private StatusType status;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    private GieglanFile gieglanFile;

    @ManyToOne(fetch = FetchType.LAZY)
    private Mesure mesure;

}
