package com.catis.objectTemporaire;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@Data @AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class LexiquePOJO {

    private String id;
    private String code;
    private String libelle;
    private String parent;
    private String haschild;
    private String visual;
    private UUID classificationId;
    private UUID version;
    private UUID categoryId;

}
