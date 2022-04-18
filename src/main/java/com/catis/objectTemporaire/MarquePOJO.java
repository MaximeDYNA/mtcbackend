package com.catis.objectTemporaire;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MarquePOJO {
    private UUID marqueVehiculeId;
    private String libelle;
    private String description;
    private UUID organisationId;
}
