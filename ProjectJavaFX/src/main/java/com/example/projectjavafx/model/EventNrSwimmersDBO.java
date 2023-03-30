package com.example.projectjavafx.model;

public class EventNrSwimmersDBO extends Event{
    private int nrSwimmers;

    public EventNrSwimmersDBO(int distance, String style, int nrSwimmers) {
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
