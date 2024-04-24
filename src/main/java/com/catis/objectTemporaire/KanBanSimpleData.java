package com.catis.objectTemporaire;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class KanBanSimpleData implements Serializable {
    private boolean contreVisite;
    private String ref;
    private int totalNumber;
}
