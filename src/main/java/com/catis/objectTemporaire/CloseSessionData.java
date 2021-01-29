package com.catis.objectTemporaire;

public class CloseSessionData {
    private Long sessionCaisseId;
    private double montantFermeture;

    public CloseSessionData(Long sessionCaisseId, double montantFermeture) {
        super();
        this.sessionCaisseId = sessionCaisseId;
        this.montantFermeture = montantFermeture;
    }

    public CloseSessionData() {
        super();
        // TODO Auto-generated constructor stub
    }

    public Long getSessionCaisseId() {
        return sessionCaisseId;
    }

    public void setSessionCaisseId(Long sessionCaisseId) {
        this.sessionCaisseId = sessionCaisseId;
    }

    public double getMontantFermeture() {
        return montantFermeture;
    }

    public void setMontantFermeture(double montantFermeture) {
        this.montantFermeture = montantFermeture;
    }

}
