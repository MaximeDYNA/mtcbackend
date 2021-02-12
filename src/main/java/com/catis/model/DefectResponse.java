package com.catis.model;

import com.catis.model.configuration.JournalData;

import java.util.List;

public class DefectResponse{

    private Long inspectionid;

    private List<DefectsModel> defectslist;

    public DefectResponse() {
    }

    public DefectResponse(Long inspection, List<DefectsModel> defectslist) {

        this.inspectionid = inspection;
        this.defectslist = defectslist;
    }

    public Long getInspectionid() {
        return inspectionid;
    }

    public void setInspectionid(Long inspectionid) {
        this.inspectionid = inspectionid;
    }

    public List<DefectsModel> getDefectslist() {
        return defectslist;
    }

    public void setDefectslist(List<DefectsModel> defectslist) {
        this.defectslist = defectslist;
    }
}
