package com.catis.model;

import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.persistence.*;

import com.catis.model.configuration.JournalData;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "t_mesurevisuel")
@EntityListeners(AuditingEntityListener.class)
@Audited
public class MesureVisuel extends JournalData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMesureVisuel;

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

    private Long gieglanFileDeleted;

    @OneToOne
    private GieglanFile gieglanFile;

    public MesureVisuel(Long idMesureVisuel, String heureDebut, String heureFin, String dateControl, String plateNumber,
            String image1, String image2, String gps, String signature1,
            String signature2, GieglanFile gieglanFile) {
        this.idMesureVisuel = idMesureVisuel;
        this.heureDebut = heureDebut;
        this.heureFin = heureFin;
        this.dateControl = dateControl;
        this.plateNumber = plateNumber;
        this.image1 = image1;
        this.image2 = image2;
        this.gps = gps;
        this.signature1 = signature1;
        this.signature2 = signature2;
        this.gieglanFile = gieglanFile;
    }

    public MesureVisuel() {
    }

    public Long getIdMesureVisuel() {
        return idMesureVisuel;
    }

    public void setIdMesureVisuel(Long idMesureVisuel) {
        this.idMesureVisuel = idMesureVisuel;
    }

    public String getHeureDebut() {
        return heureDebut;
    }

    public void setHeureDebut(String heureDebut) {
        this.heureDebut = heureDebut;
    }

    public String getHeureFin() {
        return heureFin;
    }

    public void setHeureFin(String heureFin) {
        this.heureFin = heureFin;
    }

    public String getDateControl() {
        return dateControl;
    }

    public void setDateControl(String dateControl) {
        this.dateControl = dateControl;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getGps() {
        return gps;
    }

    public void setGps(String gps) {
        this.gps = gps;
    }

    public String getSignature1() {
        return signature1;
    }

    public void setSignature1(String signature1) {
        this.signature1 = signature1;
    }

    public String getSignature2() {
        return signature2;
    }

    public void setSignature2(String signature2) {
        this.signature2 = signature2;
    }

    public GieglanFile getGieglanFile() {
        return gieglanFile;
    }

    public void setGieglanFile(GieglanFile gieglanFile) {
        this.gieglanFile = gieglanFile;
    }

    @Override
    public String toString() {
        return "MesureVisuel [idMesureVisuel=" + idMesureVisuel + ", heureDebut=" + heureDebut + ", heureFin="
                + heureFin + ", dateControl=" + dateControl + ", plateNumber=" + plateNumber + ", gps=" + gps + ", inspection=" + "]";
    }

    public Long getGieglanFileDeleted() {
        return gieglanFileDeleted;
    }

    public void setGieglanFileDeleted(Long gieglanFileDeleted) {
        this.gieglanFileDeleted = gieglanFileDeleted;
    }
}
