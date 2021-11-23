package com.catis.objectTemporaire;

import java.util.UUID;

public class CatProductForSelectDTO {
    private UUID Id;
    private String name;

    public CatProductForSelectDTO(UUID id, String name) {
        Id = id;
        this.name = name;
    }

    public UUID getId() {
        return Id;
    }

    public void setId(UUID id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
