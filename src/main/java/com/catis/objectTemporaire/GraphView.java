package com.catis.objectTemporaire;

public class GraphView {

	private String columnname;
	private int number;
	
	public GraphView() {
		super();
		// TODO Auto-generated constructor stub
	}

	public GraphView(String columnname, int number) {
		super();
		this.columnname = columnname;
		this.number = number;
	}

	public String getColumnname() {
		return columnname;
	}

	public void setColumnname(String columnname) {
		this.columnname = columnname;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}
	
}
