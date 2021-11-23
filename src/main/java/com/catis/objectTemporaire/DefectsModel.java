package com.catis.objectTemporaire;

import com.catis.model.configuration.JournalData;
import org.hibernate.envers.Audited;

import java.util.UUID;


public class DefectsModel {

    public UUID id;
    public String subcategory;
    public String defect;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
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
