package com.catis.objectTemporaire;

import java.util.UUID;

public class PosaleData {


    private String reference;
    private boolean status;
    private UUID holdId;
    private UUID produitId;
    private UUID sessionCaisseId;
    private Long number;

    public PosaleData() {

    }

    public PosaleData(String reference, boolean status, UUID holdId, UUID produitId, UUID sessionCaisseId) {
        super();
        this.reference = reference;
        this.status = status;
        this.holdId = holdId;
        this.produitId = produitId;
        this.sessionCaisseId = sessionCaisseId;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public UUID getHoldId() {
        return holdId;
    }

    public void setHoldId(UUID holdId) {
        this.holdId = holdId;
    }

    public UUID getProduitId() {
        return produitId;
    }

    public void setProduitId(UUID produitId) {
        this.produitId = produitId;
    }

    public UUID getSessionCaisseId() {
        return sessionCaisseId;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public void setSessionCaisseId(UUID sessionCaisseId) {
        this.sessionCaisseId = sessionCaisseId;
    }


}
