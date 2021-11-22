package com.catis.model.entity;

import javax.persistence.*;

import com.catis.model.configuration.JournalData;
import com.catis.model.control.GieglanFile;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.UUID;

@Entity
@Table(name = "t_mesurevisuel")
@EntityListeners(AuditingEntityListener.class)
@Audited
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
public class MesureVisuel extends JournalData {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID idMesureVisuel;

    private String heureDebut;
    private String heureFin;

    private String dateControl;

    private String plateNumber;

    @Column(columnDefinition = "LONGTEXT")
    private String image1;

    @Column(columnDefinition = "LONGTEXT")
    private String image2;
    private String gps;
    private String signature1;

    private String signature2;

    private UUID gieglanFileDeleted;

    @OneToOne
    @JsonIgnore
    private GieglanFile gieglanFile;


    @Override
    public String toString() {
        return "MesureVisuel [idMesureVisuel=" + idMesureVisuel + ", heureDebut=" + heureDebut + ", heureFin="
                + heureFin + ", dateControl=" + dateControl + ", plateNumber=" + plateNumber + ", gps=" + gps + ", inspection=" + "]";
    }

    public UUID getGieglanFileDeleted() {
        return gieglanFileDeleted;
    }

    public void setGieglanFileDeleted(UUID gieglanFileDeleted) {
        this.gieglanFileDeleted = gieglanFileDeleted;
    }
}
