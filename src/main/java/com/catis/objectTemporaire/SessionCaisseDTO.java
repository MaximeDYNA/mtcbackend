package com.catis.objectTemporaire;

import com.catis.model.entity.Caissier;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor @Data
public class SessionCaisseDTO {

    private Long sessionCaisseId;

    private Date dateHeureOuverture;

    private Date dateHeureFermeture;

    private Long caissierId;

    private double montantOuverture;

    private double montantfermeture;

    private boolean active;
}
