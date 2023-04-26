package org.example.dto;

import org.example.model.Event;

import java.io.Serializable;

public class EventDTO extends Event implements Serializable {
    private int nrSwimmers;

    public EventDTO(int distance, String style, int nrSwimmers) {
        super(distance, style);
        this.nrSwimmers = nrSwimmers;
    }

    public int getNrSwimmers() {
        return nrSwimmers;
    }

    public void setNrSwimmers(int nrSwimmers) {
        this.nrSwimmers = nrSwimmers;
    }

}
