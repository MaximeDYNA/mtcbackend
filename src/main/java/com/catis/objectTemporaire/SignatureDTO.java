package com.catis.objectTemporaire;

import java.util.UUID;

public class SignatureDTO {

    private UUID visiteId;
    private String imageValue;

    public SignatureDTO() {
    }

    public SignatureDTO(UUID visiteId, String imageValue) {
        super();
        this.visiteId = visiteId;
        this.imageValue = imageValue;
    }

    public UUID getVisiteId() {
        return visiteId;
    }

    public void setVisiteId(UUID visiteId) {
        this.visiteId = visiteId;
    }

    public String getImageValue() {
        return imageValue;
    }

    public void setImageValue(String imageValue) {
        this.imageValue = imageValue;
    }


}
