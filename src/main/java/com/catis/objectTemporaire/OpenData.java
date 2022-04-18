package com.catis.objectTemporaire;

import lombok.Data;

import java.util.UUID;
@Data
public class OpenData {

    private UUID userId;
    private double montantOuverture;
    private double montantFermeture;

    public OpenData() {
    }

    public OpenData(UUID userId, double montantOuverture, double montantFermeture) {
        this.userId = userId;
        this.montantOuverture = montantOuverture;
        this.montantFermeture = montantFermeture;
    }


}
