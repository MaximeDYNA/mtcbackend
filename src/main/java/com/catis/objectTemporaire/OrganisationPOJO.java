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
public class OrganisationPOJO {
      private UUID id;
      private String nom;
      private String adresse;
      private String lang;
      private String devise;
      private String region;
      private String numContribuable;
      private String tel1;
      private String tel2;
      private boolean parent;
      private UUID parentOrganisation;
}
