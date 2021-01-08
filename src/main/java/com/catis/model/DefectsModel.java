package com.catis.model;

public class DefectsModel {
	
	public int id;
    public String subcategory;
    public String defect;
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
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
