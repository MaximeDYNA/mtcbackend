package com.catis.objectTemporaire;

import lombok.Data;

import java.util.UUID;
@Data
public class HoldData {

    private UUID sessionCaisseId;
    private UUID number;

    public HoldData() {
        super();
        // TODO Auto-generated constructor stub
    }

    public HoldData(UUID sessionCaisseId, UUID number) {
        super();
        this.sessionCaisseId = sessionCaisseId;
        this.number = number;
    }



    public boolean isValid() {
        if (this.sessionCaisseId == null || this.number == null) {
            return false;
        }
        return true;
    }

}
