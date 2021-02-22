package com.catis.model;

import com.catis.model.configuration.JournalData;

import java.util.List;

public class DefectResponse{

    private Long idinspection;

    private String signature1;

    private String signature2;

    private List<DefectsModel> defectslist;

    public DefectResponse() {
    }

    public DefectResponse(Long inspection, List<DefectsModel> defectslist) {

        this.idinspection = inspection;
        this.defectslist = defectslist;
    }

    public Long getIdinspection() {
        return idinspection;
    }

    public void setIdinspection(Long idinspection) {
        this.idinspection = idinspection;
    }

    public List<DefectsModel> getDefectslist() {
        return defectslist;
    }

    public void setDefectslist(List<DefectsModel> defectslist) {
        this.defectslist = defectslist;
    }

    public String getSignature1() {
        return signature1;
    }

    public void setSignature1(String signature1) {
        this.signature1 = signature1;
    }

    public String getSignature2() {
        return signature2;
    }

    public void setSignature2(String signature2) {
        this.signature2 = signature2;
    }

    @Override
    public String toString() {
        return "DefectResponse{" +
                "idinspection=" + idinspection +
                ", signature1='" + signature1 + '\'' +
                ", signature2='" + signature2 + '\'' +
                ", defectslist=" + defectslist +
                '}';
    }
}
