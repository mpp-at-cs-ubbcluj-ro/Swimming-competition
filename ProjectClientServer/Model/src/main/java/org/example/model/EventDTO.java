package org.example.model;

import java.io.Serializable;

public class EventDTO extends Entity<Integer> implements Serializable {
    private int nrSwimmers;
    private int distance;
    private String style;

    public EventDTO(int distance, String style, int nrSwimmers) {
        this.distance = distance;
        this.style = style;
        this.nrSwimmers = nrSwimmers;
    }

    public EventDTO()
    {

    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public int getNrSwimmers() {
        return nrSwimmers;
    }

    public void setNrSwimmers(int nrSwimmers) {
        this.nrSwimmers = nrSwimmers;
    }

}
