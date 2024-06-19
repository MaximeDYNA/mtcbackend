package com.catis.objectTemporaire;

import com.catis.model.configuration.JournalData;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.hibernate.envers.Audited;

import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DefectsModel {

    public String id;
    public String subcategory;
    public String defect;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public String getDefect() {
        return defect;
    }

    public void setDefect(String defect) {
        this.defect = defect;
    }

}
