package com.catis.objectTemporaire;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrganisationDTO {
    private Long id;
    private String nom;
    private String adresse;
    private String tel1;
    private String tel2;
    private boolean parent;
    private ModelForSelectDTO parentOrganisation;
}
