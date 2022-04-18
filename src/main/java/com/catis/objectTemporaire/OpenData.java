package com.catis.objectTemporaire;

import lombok.Data;

@Data
public class OpenData {

    private String userId;
    private double montantOuverture;
    private double montantFermeture;

    public OpenData() {
    }

    public OpenData(String userId, double montantOuverture, double montantFermeture) {
        this.userId = userId;
        this.montantOuverture = montantOuverture;
        this.montantFermeture = montantFermeture;
    }


}
