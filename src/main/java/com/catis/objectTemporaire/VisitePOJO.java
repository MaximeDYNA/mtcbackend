package com.catis.objectTemporaire;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VisitePOJO {
    private Long idVisite;
    private boolean contreVisite;
    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;
    private Long dateD;
    private Long dateF;
    private boolean encours = true;
    private String statut;
    private ObjectForSelect caissier;
    private ObjectForSelect carteGrise;
    private ObjectForSelect organisationId;
}
