package com.catis.objectTemporaire;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FormulePOJO {
    private Long id;
    private String description;
    private ObjectForSelect organisationId;
}
