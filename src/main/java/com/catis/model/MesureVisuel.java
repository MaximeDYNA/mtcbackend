package com.catis.model;

import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.persistence.*;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "t_mesurevisuel")
@EntityListeners(AuditingEntityListener.class)
public class MesureVisuel {

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

    @OneToOne(cascade = CascadeType.ALL)
    private Inspection inspection;

    public MesureVisuel() {

    }

    public MesureVisuel(Long idMesureVisuel, String heureDebut, String heureFin, String dateControl
            , String image1, String image2, String plateNumber, Inspection inspection) {

        super();
        this.idMesureVisuel = idMesureVisuel;
        this.heureDebut = heureDebut;
        this.heureFin = heureFin;
        this.plateNumber = plateNumber;
        this.dateControl = dateControl;
        this.image1 = image1;
        this.image2 = image2;

        this.inspection = inspection;

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

    public Inspection getInspection() {
        return inspection;
    }

    public void setInspection(Inspection inspection) {
        this.inspection = inspection;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getGps() {
        return gps;
    }

    public void setGps(String gps) {
        this.gps = gps;
    }

    @Override
    public String toString() {
        return "MesureVisuel [idMesureVisuel=" + idMesureVisuel + ", heureDebut=" + heureDebut + ", heureFin="
                + heureFin + ", dateControl=" + dateControl + ", plateNumber=" + plateNumber + ", gps=" + gps + ", inspection=" + inspection + "]";
    }

}
