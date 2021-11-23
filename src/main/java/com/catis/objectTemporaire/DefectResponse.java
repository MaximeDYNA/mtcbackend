package com.catis.objectTemporaire;

import com.catis.objectTemporaire.DefectsModel;
import org.hibernate.envers.Audited;

import java.util.List;
import java.util.UUID;

public class DefectResponse{

    private UUID idinspection;

    private String signature1;

    private String signature2;

    private List<DefectsModel> defectslist;

    public DefectResponse() {
    }

    public DefectResponse(UUID inspection, List<DefectsModel> defectslist) {

        this.idinspection = inspection;
        this.defectslist = defectslist;
    }

    public UUID getIdinspection() {
        return idinspection;
    }

    public void setIdinspection(UUID idinspection) {
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
