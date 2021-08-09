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

    private String appreciation;

    private String depreciation;

    private ObjectForSelect fraudeType;

    private ObjectForSelect intervenantFraude;
}
