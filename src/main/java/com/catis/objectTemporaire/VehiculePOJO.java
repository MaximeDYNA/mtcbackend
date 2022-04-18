package com.catis.objectTemporaire;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VehiculePOJO {
    private UUID vehiculeId;
    private String typeVehicule;
    private String carrosserie;
    private int placeAssise;
    private String chassis;
    private Date premiereMiseEnCirculation;
    private int puissAdmin;
    private int poidsTotalCha;
    private int poidsVide;
    private int chargeUtile;
    private int cylindre;
    private UUID marqueVehicule;
    private UUID energie;
    private ObjectForSelect organisationId;
}
