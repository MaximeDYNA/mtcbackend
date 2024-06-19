package com.catis.objectTemporaire;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.UUID;

@AllArgsConstructor @NoArgsConstructor
@Data @ToString

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProduitVue {

    private UUID produitId;
    private String reference;





}
