package org.example.model;

import java.util.Objects;

public class Event extends Entity<Long> {
    private Integer distance;
    private Style style;

    public Event(Integer distance, Style style) {
        this.distance = distance;
        this.style = style;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public Style getStyle() {
        return style;
    }

    public void setStyle(Style style) {
        this.style = style;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event task = (Event) o;
        return Objects.equals(distance, task.distance) && style == task.style;
    }

    @Override
    public int hashCode() {
        return Objects.hash(distance, style);
    }


}
