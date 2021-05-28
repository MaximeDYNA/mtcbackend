package com.catis.model;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.model.configuration.JournalData;

@Entity
@Table(name = "t_modelevehicule")
@EntityListeners(AuditingEntityListener.class)
@Audited
public class ModeleVehicule extends JournalData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idModele;
    private String description;



    public ModeleVehicule() {

    }

    public ModeleVehicule(Long idModele, String description) {
        this.idModele = idModele;
        this.description = description;

    }

    public Long getIdModele() {
        return idModele;
    }

    public void setIdModele(Long idModele) {
        this.idModele = idModele;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
