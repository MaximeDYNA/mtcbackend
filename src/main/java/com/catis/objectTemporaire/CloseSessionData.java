package com.catis.objectTemporaire;

import java.util.UUID;

public class CloseSessionData {
    private UUID sessionCaisseId;
    private double montantFermeture;

    public CloseSessionData(UUID sessionCaisseId, double montantFermeture) {
        super();
        this.sessionCaisseId = sessionCaisseId;
        this.montantFermeture = montantFermeture;
    }

    public CloseSessionData() {
        super();
        // TODO Auto-generated constructor stub
    }

    public UUID getSessionCaisseId() {
        return sessionCaisseId;
    }

    public void setSessionCaisseId(UUID sessionCaisseId) {
        this.sessionCaisseId = sessionCaisseId;
    }

    public double getMontantFermeture() {
        return montantFermeture;
    }

    public void setMontantFermeture(double montantFermeture) {
        this.montantFermeture = montantFermeture;
    }

}
