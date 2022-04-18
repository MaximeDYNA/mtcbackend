package com.catis.objectTemporaire;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class RecapDTO {

    private String caissierId;
    private String dateDebut;
    private String dateFin;

    public RecapDTO() {
    }


}
