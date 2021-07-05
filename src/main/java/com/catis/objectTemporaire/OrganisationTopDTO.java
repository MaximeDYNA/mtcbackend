package com.catis.objectTemporaire;

import com.catis.model.entity.Organisation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrganisationTopDTO {
    private Organisation organisation;
    private int value;
    private int pourcentage;


}
