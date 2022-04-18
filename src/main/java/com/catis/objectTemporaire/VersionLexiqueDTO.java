package com.catis.objectTemporaire;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class VersionLexiqueDTO {

    private UUID id;
    private String libelle;
    private String version;
    private String createdDate;
    private String modifiedDate;


    public VersionLexiqueDTO(String libelle, String version, String createdDate, String modifiedDate) {
        super();
        this.libelle = libelle;
        this.version = version;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

    public VersionLexiqueDTO() {
        super();
        // TODO Auto-generated constructor stub
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedString = createdDate.format(formatter);
        this.createdDate = formattedString;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(LocalDateTime modifiedDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedString = modifiedDate.format(formatter);
        this.modifiedDate = formattedString;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }


}
