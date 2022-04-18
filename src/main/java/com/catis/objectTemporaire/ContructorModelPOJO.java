package com.catis.objectTemporaire;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContructorModelPOJO {

    private UUID id;
    private String name;
    private ObjectForSelect organisationId;
    private ObjectForSelect constructor;
}
