package com.catis.objectTemporaire;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MachinePOJO {
    private Long idMachine;
    private String numSerie; // numéro de série
    private String fabriquant;
    private String model;
}
