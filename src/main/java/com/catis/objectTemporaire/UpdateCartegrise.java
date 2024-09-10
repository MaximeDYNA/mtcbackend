package com.catis.objectTemporaire;

import java.util.UUID;

import lombok.AllArgsConstructor;

import lombok.Getter;

import lombok.NoArgsConstructor;

import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateCartegrise {
    private UUID carteGriseId;
    private UUID newProprietaireVehiculeId;
}
