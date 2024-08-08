package com.catis.control.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class conformityResponse {

    private boolean success;

    private String message;

    public conformityResponse(boolean success) {
        this.success = success;
        this.message = success
            ? "conformity is correct"
            : "conformity isn't correct";
    }
}
