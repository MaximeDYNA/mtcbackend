package com.catis.objectTemporaire;

public class ExpectedMeasure {

	private String name;
	private String icon;
	private boolean isFinished;
	
	public ExpectedMeasure(String name, String icon, boolean isFinished) {
		super();
		this.name = name;
		this.icon = icon;
		this.isFinished = isFinished;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public boolean isFinished() {
		return isFinished;
	}

	public void setFinished(boolean isFinished) {
		this.isFinished = isFinished;
	}
	
	
	
}
