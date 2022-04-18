package com.catis.objectTemporaire;

import java.util.List;
import java.util.UUID;

public class LexiquePOJO {

    private UUID id;
    private String code;
    private String libelle;
    private String parent;
    private String haschild;
    private String visual;
    private UUID classificationId;
    private UUID version;
    private UUID categoryId;

    public LexiquePOJO() {
        super();
        // TODO Auto-generated constructor stub
    }

    public LexiquePOJO(UUID id, String code, String libelle, String parent, String haschild, String visual,
                       UUID version, UUID categoryId) {
        super();
        this.id = id;
        this.code = code;
        this.libelle = libelle;
        this.parent = parent;
        this.haschild = haschild;
        this.visual = visual;
        this.version = version;

        this.categoryId = categoryId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getHaschild() {
        return haschild;
    }

    public void setHaschild(String haschild) {
        this.haschild = haschild;
    }

    public String getVisual() {
        return visual;
    }

    public void setVisual(String visual) {
        this.visual = visual;
    }

    public UUID getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(UUID categoryId) {
        this.categoryId = categoryId;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getVersion() {
        return version;
    }

    public void setVersion(UUID version) {
        this.version = version;
    }

    public UUID getClassificationId() {
        return classificationId;
    }

    public void setClassificationId(UUID classificationId) {
        this.classificationId = classificationId;
    }

}
