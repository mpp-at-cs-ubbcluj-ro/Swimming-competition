package com.example.projectjavafx.model;

import javafx.scene.control.CheckBox;

import java.nio.channels.AsynchronousChannel;
import java.util.Objects;

public class Event extends Entity<Integer> {
    private Integer distance;
    private String style;
    private CheckBox select;
    public Event(Integer distance, String style) {
        this.distance = distance;
        this.style = style;
        this.select = new CheckBox();
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
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

    @Override
    public String toString() {
        return distance +
                "," + style + "\n";
    }

    public CheckBox getSelect() {
        return select;
    }
    public void setSelect(CheckBox select) {
        this.select = select;
    }
}
