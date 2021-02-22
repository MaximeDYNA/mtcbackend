package com.catis.objectTemporaire;

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public double getMontantOuverture() {
        return montantOuverture;
    }

    public void setMontantOuverture(double montantOuverture) {
        this.montantOuverture = montantOuverture;
    }

    public double getMontantFermeture() {
        return montantFermeture;
    }

    public void setMontantFermeture(double montantFermeture) {
        this.montantFermeture = montantFermeture;
    }


}
