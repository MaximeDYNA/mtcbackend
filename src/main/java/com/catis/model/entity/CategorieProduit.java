package com.catis.model.entity;


import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.model.configuration.JournalData;
import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "t_categorieproduit")
@Audited
@SQLDelete(sql = "UPDATE t_categorieproduit SET active_status=false WHERE categorie_produit_id=?")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class CategorieProduit extends JournalData{

    @Id
    @GeneratedValue(generator = "UUID")
    @Type(type="uuid-char")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID categorieProduitId;
    private String libelle;
    private String description;

    @OneToMany(mappedBy = "categorieProduit",fetch = FetchType.LAZY)
    @JsonIgnore
    Set<Produit> produits;



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CategorieProduit)) return false;
        CategorieProduit that = (CategorieProduit) o;
        return Objects.equals(getCategorieProduitId(), that.getCategorieProduitId());
    }

}
