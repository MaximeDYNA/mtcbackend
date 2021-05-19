package com.catis.model;

import com.catis.model.configuration.JournalData;
import org.hibernate.envers.Audited;

@Audited
public class DefectsModel {

    public Long id;
    public String subcategory;
    public String defect;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
