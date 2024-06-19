package com.catis.model.entity;

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

import java.util.UUID;

@Entity
@Table(name = "t_operationdecaisse")
@EntityListeners(AuditingEntityListener.class)
@Audited
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class OperationCaisse extends JournalData {
    @Id
    @GeneratedValue(generator = "UUID")
    @Type(type="uuid-char")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID operationDeCaisseId;
    private int type;
    private double montant;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE},fetch = FetchType.LAZY)
    private Vente vente;

    @ManyToOne(fetch = FetchType.LAZY)
    private Taxe taxe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private SessionCaisse sessionCaisse;

    private String numeroTicket;


    public String getLibelle() {
        if (this.type == 0) {
            return "Mise en compte";
        }
        if (this.type == 1) {
            return "Encaissement";
        }
        if (this.type == 2) {
            return "Décaissement";
        }
        return "Erreur";
    }

}
