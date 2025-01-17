package com.catis.model.entity;

import java.util.Set;


import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.model.configuration.JournalData;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Audited
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class Formule extends JournalData {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    private String description;

    @OneToMany(mappedBy = "formule",fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Mesure> mesures;

    @OneToMany(mappedBy = "formule", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Seuil> seuils;


}