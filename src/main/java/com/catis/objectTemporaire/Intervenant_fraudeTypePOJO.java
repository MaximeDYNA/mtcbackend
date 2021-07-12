package com.catis.objectTemporaire;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Intervenant_fraudeTypePOJO {

    private Long id;

    private double appreciation = 0.0;

    private double depreciation = 0.0;

    private String fraudeType;

    private ObjectForSelect intervenantFraude;
}
