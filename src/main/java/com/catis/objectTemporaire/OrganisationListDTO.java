package com.catis.objectTemporaire;

public class OrganisationListDTO {
    private Long id;
    private String name;
    private Long categorieProduitId;

    public OrganisationListDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public OrganisationListDTO() {
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

    public Long getCategorieProduitId() {
        return categorieProduitId;
    }

    public void setCategorieProduitId(Long categorieProduitId) {
        this.categorieProduitId = categorieProduitId;
    }
}
