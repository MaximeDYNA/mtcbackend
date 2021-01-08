package com.catis.model;

import java.util.List;

public class defectresponse {

	private int inspectionid;
	private List<DefectsModel> defectslist;
	
	public defectresponse(int inspection,List<DefectsModel> defectslist ) {
		
		this.inspectionid = inspection;
		this.defectslist = defectslist;
	}

	public int getInspectionid() {
		return inspectionid;
	}

	public void setInspectionid(int inspectionid) {
		this.inspectionid = inspectionid;
	}

	public List<DefectsModel> getDefectslist() {
		return defectslist;
	}

	public void setDefectslist(List<DefectsModel> defectslist) {
		this.defectslist = defectslist;
	}

	
	
	
}
