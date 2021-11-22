package com.catis.model.entity;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.model.configuration.JournalData;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.UUID;

@Entity
@Table(name = "t_utilisateur")
@Audited
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE t_utilisateur SET active_status=false WHERE utilisateur_id=?")
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class Utilisateur extends JournalData {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID utilisateurId;

    private String keycloakId;

    private String login;

    @OneToOne(mappedBy = "utilisateur")
    @JsonIgnore
    private Controleur controleur;






}
