package com.catis.objectTemporaire;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TestNew {
    private UUID id;
    private String icon;
    private String extension;
}
