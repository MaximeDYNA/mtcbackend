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
public class LignePOJO {

    private UUID idLigne;
    private String description;
    private String nom;
    private ObjectForSelect organisationId;

}
