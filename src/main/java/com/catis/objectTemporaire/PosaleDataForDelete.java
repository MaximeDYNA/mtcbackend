package com.catis.objectTemporaire;

import java.util.UUID;

public class PosaleDataForDelete {

    private String reference;
    private UUID sessionCaisseId;

    public PosaleDataForDelete(String reference, UUID sessionCaisseId) {
        super();
        this.reference = reference;
        this.sessionCaisseId = sessionCaisseId;
    }

    public PosaleDataForDelete() {
        super();
        // TODO Auto-generated constructor stub
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public UUID getSessionCaisseId() {
        return sessionCaisseId;
    }

    public void setSessionCaisseId(UUID sessionCaisseId) {
        this.sessionCaisseId = sessionCaisseId;
    }

}
