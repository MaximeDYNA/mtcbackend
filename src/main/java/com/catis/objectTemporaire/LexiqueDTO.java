package com.catis.objectTemporaire;

import java.util.List;

public class LexiqueDTO {

	private Long id;
	private String name;
	
	private List<LexiqueChildDTO> children;

	
	public LexiqueDTO() {
		super();
		// TODO Auto-generated constructor stub
	}


	public LexiqueDTO(Long id, String name, List<LexiqueChildDTO> children) {
		super();
		this.id = id;
		this.name = name;
		this.children = children;
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public List<LexiqueChildDTO> getChildren() {
		return children;
	}


	public void setChildren(List<LexiqueChildDTO> children) {
		this.children = children;
	}
	
	
}
