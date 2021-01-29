package com.catis.objectTemporaire;

public class SignatureDTO {

    private Long visiteId;
    private String imageValue;

    public SignatureDTO() {
    }

    public SignatureDTO(Long visiteId, String imageValue) {
        super();
        this.visiteId = visiteId;
        this.imageValue = imageValue;
    }

    public Long getVisiteId() {
        return visiteId;
    }

    public void setVisiteId(Long visiteId) {
        this.visiteId = visiteId;
    }

    public String getImageValue() {
        return imageValue;
    }

    public void setImageValue(String imageValue) {
        this.imageValue = imageValue;
    }


}
