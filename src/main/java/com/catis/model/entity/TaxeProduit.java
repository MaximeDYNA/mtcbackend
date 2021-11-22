package com.catis.model.entity;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.model.configuration.JournalData;

import java.util.UUID;

@Entity
@Table(name = "t_taxeproduit")
@EntityListeners(AuditingEntityListener.class)
@Audited
@Getter
@Setter
@AllArgsConstructor @NoArgsConstructor
public class TaxeProduit extends JournalData {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID TaxeProduitId;
    @ManyToOne
    private Taxe taxe;
    @ManyToOne
    private Produit produit;


}
