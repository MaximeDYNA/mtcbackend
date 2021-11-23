package com.catis.objectTemporaire;

import java.util.UUID;

public class RecapDTO {

    private UUID caissierId;
    private String dateDebut;
    private String dateFin;

    public RecapDTO() {
    }

    public RecapDTO(UUID caissierId, String dateDebut, String dateFin) {
        super();
        this.caissierId = caissierId;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
    }

    public UUID getCaissierId() {
        return caissierId;
    }

    public void setCaissierId(UUID caissierId) {
        this.caissierId = caissierId;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }


}
