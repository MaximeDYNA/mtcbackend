package com.catis.objectTemporaire;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContructorModelPOJO {

    private Long id;
    private String name;
    private ObjectForSelect organisationId;
    private ObjectForSelect constructor;
}
