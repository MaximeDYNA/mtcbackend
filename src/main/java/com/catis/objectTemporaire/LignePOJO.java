package com.catis.objectTemporaire;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LignePOJO {

    private Long idLigne;
    private String description;
    private String nom;
    private ObjectForSelect organisationId;

}
