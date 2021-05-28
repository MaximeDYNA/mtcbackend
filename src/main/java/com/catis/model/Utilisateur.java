package com.catis.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.model.configuration.JournalData;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "t_utilisateur")
@Audited
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE t_utilisateur SET active_status=false WHERE utilisateur_id=?")
public class Utilisateur extends JournalData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long utilisateurId;

    private String keycloakId;


    public Utilisateur() {
    }


    public Utilisateur(Long utilisateurId, String keycloakId) {
        super();
        this.utilisateurId = utilisateurId;
        this.keycloakId = keycloakId;

    }


    public Long getUtilisateurId() {
        return utilisateurId;
    }


    public void setUtilisateurId(Long utilisateurId) {
        this.utilisateurId = utilisateurId;
    }


    public String getKeycloakId() {
        return keycloakId;
    }


    public void setKeycloakId(String keycloakId) {
        this.keycloakId = keycloakId;
    }



}
