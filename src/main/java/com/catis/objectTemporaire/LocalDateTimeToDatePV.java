package com.catis.objectTemporaire;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class LocalDateTimeToDatePV {
    public static Date convert(LocalDateTime dateToConvert) {
        return java.util.Date
                .from(dateToConvert.atZone(ZoneId.systemDefault())
                        .toInstant());
    }
}
