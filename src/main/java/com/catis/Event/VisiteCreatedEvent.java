package com.catis.Event;

import com.catis.model.Visite;

public class VisiteCreatedEvent {

    private Visite visite;

    public VisiteCreatedEvent(Visite visite) {
        this.visite = visite;
    }

    public Visite getVisite() {
        return visite;
    }
}
