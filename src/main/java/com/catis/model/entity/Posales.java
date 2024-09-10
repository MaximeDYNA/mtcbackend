package com.catis.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.model.configuration.JournalData;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;

import java.util.UUID;

@Entity
@Table(name = "t_posales")
@EntityListeners(AuditingEntityListener.class)
@Audited
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class Posales extends JournalData {

    @Id
    @GeneratedValue(generator = "UUID")
    @Type(type="uuid-char")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID posalesId;

    @ManyToOne
    private Produit produit;


    private boolean status;


    @ManyToOne(fetch = FetchType.LAZY)
    private SessionCaisse sessionCaisse;


    @ManyToOne(fetch = FetchType.LAZY)
    private Hold hold;


    @NotNull
    @NotEmpty(message = "La référence ne peut être vide")
    @Column(unique = true, nullable = false)
    private String reference;


    @JsonIgnore
    public SessionCaisse getSessionCaisse() {
        return sessionCaisse;
    }


}
