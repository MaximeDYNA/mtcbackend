package com.catis.objectTemporaire;

import java.util.UUID;

public class HoldData {

    private UUID sessionCaisseId;
    private Long number;

    public HoldData() {
        super();
        // TODO Auto-generated constructor stub
    }

    public HoldData(UUID sessionCaisseId, Long number) {
        super();
        this.sessionCaisseId = sessionCaisseId;
        this.number = number;
    }

    public UUID getSessionCaisseId() {
        return sessionCaisseId;
    }

    public void setSessionCaisseId(UUID sessionCaisseId) {
        this.sessionCaisseId = sessionCaisseId;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public boolean isValid() {
        if (this.sessionCaisseId == null || this.number == null) {
            return false;
        }
        return true;
    }

}
