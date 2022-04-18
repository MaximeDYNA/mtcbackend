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
public class Intervenant_fraudeTypePOJO {

    private UUID id;

    private String appreciation;

    private String depreciation;

    private ObjectForSelect fraudeType;

    private ObjectForSelect intervenantFraude;
}
