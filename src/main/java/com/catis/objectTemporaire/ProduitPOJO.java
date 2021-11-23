package com.catis.objectTemporaire;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProduitPOJO {
    private UUID id;
    private String libelle;
    private String description;
    private double prix;
    private int delaiValidite;
    private String img;
    private UUID categorieProduitId;
}
