package com.catis.objectTemporaire;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@AllArgsConstructor @NoArgsConstructor
public class HoldDTO {
    private Long holdId;
    private Long number;
    private Date time;
}
