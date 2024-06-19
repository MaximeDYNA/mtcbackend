package com.catis.model.control;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.*;

import com.catis.model.entity.CarteGrise;
import com.catis.model.entity.Visite;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.model.configuration.JournalData;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Audited
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class Control extends JournalData {

    @Id
    @GeneratedValue(generator = "UUID")
    @Type(type="uuid-char")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Enumerated(EnumType.STRING)
    private StatusType status;

    private LocalDateTime contreVDelayAt;

    private LocalDateTime validityAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private CarteGrise carteGrise;

    @OneToMany(mappedBy = "control", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Visite> visites = new ArrayList<>();


    public enum StatusType {
        INITIALIZED, REJECTED, VALIDATED
    }





}