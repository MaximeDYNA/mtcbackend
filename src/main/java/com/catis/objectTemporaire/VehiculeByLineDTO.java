package com.catis.objectTemporaire;

import java.io.Serializable;
import java.util.UUID;

public class VehiculeByLineDTO implements Serializable {

    private UUID carteGriseId;
    private String ref;
    private UUID idInspection;
    private UUID idCategorie;
    private String certidocsId;
    private boolean inspected;


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

    public boolean isInspected() {
        return inspected;
    }

    public void setInspected(boolean inspected) {
        this.inspected = inspected;
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

    public String getCertidocsId() {
        return certidocsId;
    }

    public void setCertidocsId(String certidocsId) {
        this.certidocsId = certidocsId;
    }

    @Override
    public String toString() {
        return "VehiculeByLineDTO [carteGriseId=" + carteGriseId + ", ref=" + ref + ", idInspection=" + idInspection
                + ", idCategorie=" + idCategorie + "]";
    }


}
