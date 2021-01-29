package com.catis.model;

import java.util.List;

public class defectresponse {

    private Long inspectionid;

    private List<DefectsModel> defectslist;

    public defectresponse(Long inspection, List<DefectsModel> defectslist) {

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
