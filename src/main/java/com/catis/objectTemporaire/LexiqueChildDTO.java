package com.catis.objectTemporaire;

import java.util.UUID;

public class LexiqueChildDTO {

    private UUID id;
    private String name;


    public LexiqueChildDTO() {
        super();
        // TODO Auto-generated constructor stub
    }


    public LexiqueChildDTO(UUID id, String name) {
        super();
        this.id = id;
        this.name = name;
    }


    public UUID getId() {
        return id;
    }


    public void setId(UUID id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

}
