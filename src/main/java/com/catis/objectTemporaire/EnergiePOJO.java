package com.catis.objectTemporaire;

import com.catis.model.Organisation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EnergiePOJO {
    private Long energieId;
    private String libelle;
    private Long organisationId;
}