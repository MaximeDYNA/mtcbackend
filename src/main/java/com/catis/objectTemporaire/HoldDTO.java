package com.catis.objectTemporaire;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor @NoArgsConstructor
public class HoldDTO {
    private UUID holdId;
    private Long number;
    private Date time;
}
