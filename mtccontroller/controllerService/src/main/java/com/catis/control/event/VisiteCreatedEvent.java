package com.catis.control.event;

import com.catis.control.entities.Visite;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public class VisiteCreatedEvent {

    private final Visite visite;
}
