package com.catis.objectTemporaire;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DaschBoardLogDTO {
    private String author;
    private String action;
    private String entity;
    private Long entityId;
    private LocalDateTime time;
}
