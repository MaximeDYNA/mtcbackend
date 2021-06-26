package com.catis.objectTemporaire;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SeuilPOJO {

    private Long id;
    private double value;
    private String operande;
    private String codeMessage;
    private boolean decision;
    private ObjectForSelect lexique;
    private ObjectForSelect formule;
    private ObjectForSelect organisationId;
}
