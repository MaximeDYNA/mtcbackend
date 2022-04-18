package com.catis.objectTemporaire;

import com.catis.model.entity.Caissier;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor @Data
public class SessionCaisseDTO {

    private UUID sessionCaisseId;

    private Date dateHeureOuverture;

    private Date dateHeureFermeture;

    private UUID caissierId;

    private double montantOuverture;

    private double montantfermeture;

    private boolean active;
}
