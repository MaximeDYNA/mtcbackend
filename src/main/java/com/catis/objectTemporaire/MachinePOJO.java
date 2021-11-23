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
public class MachinePOJO {
    private UUID idMachine;
    private String numSerie; // numéro de série
    private String fabriquant;
    private String model;
    private ObjectForSelect constructorModel;
    private ObjectForSelect organisationId;
}
