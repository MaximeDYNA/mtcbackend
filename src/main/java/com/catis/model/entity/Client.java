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
import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@Table(name = "t_client")
@EntityListeners(AuditingEntityListener.class)
@Audited
@SQLDelete(sql = "UPDATE t_client SET active_status=false WHERE client_id=?")
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class Client extends JournalData {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Id
    @GeneratedValue(generator = "UUID")
    @Type(type="uuid-char")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID clientId;
    private String description;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE },fetch = FetchType.LAZY)
    private Partenaire partenaire;

    @ManyToMany(fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Lexique> lexiques;

    @OneToMany(mappedBy = "client",fetch = FetchType.LAZY)
    @JsonIgnore
    Set<Vente> ventes;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Client)) return false;
        Client client = (Client) o;
        return Objects.equals(getClientId(), client.getClientId());
    }


}
