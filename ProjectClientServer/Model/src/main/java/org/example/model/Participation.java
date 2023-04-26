package org.example.model;

import java.awt.*;
import java.io.Serializable;
import java.util.Objects;

public class Participation extends Entity<Integer> implements Serializable {
    private Swimmer Swimmer;
    private Event Event;

    public Participation(Swimmer swimmer, Event event) {
        Swimmer = swimmer;
        Event = event;
    }

    public Swimmer getSwimmer() {
        return Swimmer;
    }

    public void setSwimmer(Swimmer swimmer) {
        Swimmer = swimmer;
    }

    public Event getEvent() {
        return Event;
    }

    public void setEvent(Event event) {
        Event = event;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Participation that = (Participation) o;
        return Objects.equals(Swimmer, that.Swimmer) && Objects.equals(Event, that.Event);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Swimmer, Event);
    }

    @Override
    public String toString() {
        return "Participation{" +
                "Swimmer=" + Swimmer +
                ", Event=" + Event +
                '}';
    }
}
