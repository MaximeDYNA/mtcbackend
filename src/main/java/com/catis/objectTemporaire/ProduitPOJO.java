package com.catis.objectTemporaire;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProduitPOJO {
    private String libelle;
    private String description;
    private double prix;
    private int delaiValidite;
    private String img;
    private Long categorieProduitId;
}
