package com.catis.objectTemporaire;

import java.util.UUID;

public class VehiculeByLineDTO {

    private UUID carteGriseId;
    private String ref;
    private UUID idInspection;
    private UUID idCategorie;


    public VehiculeByLineDTO() {
        super();
        // TODO Auto-generated constructor stub
    }


    public VehiculeByLineDTO(UUID carteGriseId, String ref, UUID idInspection, UUID idCategorie) {
        super();
        this.carteGriseId = carteGriseId;
        this.ref = ref;
        this.idInspection = idInspection;
        this.idCategorie = idCategorie;
    }


    public UUID getCarteGriseId() {
        return carteGriseId;
    }

    public void setCarteGriseId(UUID carteGriseId) {
        this.carteGriseId = carteGriseId;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public UUID getIdInspection() {
        return idInspection;
    }

    public void setIdInspection(UUID idInspection) {
        this.idInspection = idInspection;
    }


    public UUID getIdCategorie() {
        return idCategorie;
    }


    public void setIdCategorie(UUID idCategorie) {
        this.idCategorie = idCategorie;
    }


    @Override
    public String toString() {
        return "VehiculeByLineDTO [carteGriseId=" + carteGriseId + ", ref=" + ref + ", idInspection=" + idInspection
                + ", idCategorie=" + idCategorie + "]";
    }


}
