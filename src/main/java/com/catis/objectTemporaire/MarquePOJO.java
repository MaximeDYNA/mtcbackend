package com.catis.objectTemporaire;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MarquePOJO {
    private Long marqueVehiculeId;
    private String libelle;
    private String description;
    private Long organisationId;
}
