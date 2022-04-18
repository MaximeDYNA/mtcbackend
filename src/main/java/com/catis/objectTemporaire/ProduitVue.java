package com.catis.objectTemporaire;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@AllArgsConstructor @NoArgsConstructor
@Data @ToString


public class ProduitVue {

    private UUID produitId;
    private String reference;





}
