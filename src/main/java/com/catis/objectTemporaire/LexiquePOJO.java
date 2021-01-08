package com.catis.objectTemporaire;

public class LexiquePOJO {

	private Long id;
	private String code;
	private String libelle;
	private String parent;
	private String haschild;
	private String visual;	
	private Long version;	
	private int clientId;
	private int categoryId;
	
	
	public LexiquePOJO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

	public LexiquePOJO(Long id, String code, String libelle, String parent, String haschild, String visual,
			Long version, int clientId, int categoryId) {
		super();
		this.id = id;
		this.code = code;
		this.libelle = libelle;
		this.parent = parent;
		this.haschild = haschild;
		this.visual = visual;
		this.version = version;
		this.clientId = clientId;
		this.categoryId = categoryId;
	}



	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getLibelle() {
		return libelle;
	}
	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}
	public String getParent() {
		return parent;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}
	
	

	public String getHaschild() {
		return haschild;
	}



	public void setHaschild(String haschild) {
		this.haschild = haschild;
	}



	public String getVisual() {
		return visual;
	}



	public void setVisual(String visual) {
		this.visual = visual;
	}



	public int getClientId() {
		return clientId;
	}
	public void setClientId(int clientId) {
		this.clientId = clientId;
	}
	public int getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getVersion() {
		return version;
	}
	public void setVersion(Long version) {
		this.version = version;
	}
	
	

}
