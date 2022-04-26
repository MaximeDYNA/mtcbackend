package com.catis.objectTemporaire;

import java.util.List;
import java.util.UUID;

public class LexiqueDTO {

    private String id;
    private String name;

    private List<LexiqueChildDTO> children;


    public LexiqueDTO() {
        super();
        // TODO Auto-generated constructor stub
    }


    public LexiqueDTO(String id, String name, List<LexiqueChildDTO> children) {
        super();
        this.id = id;
        this.name = name;
        this.children = children;
    }


    public String getId() {
        return id;
    }


    public void setId(String id) {
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
