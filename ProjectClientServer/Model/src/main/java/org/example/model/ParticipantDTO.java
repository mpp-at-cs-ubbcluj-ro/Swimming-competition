package org.example.model;

import java.io.Serializable;
import java.util.List;

public class ParticipantDTO implements Serializable {
    private String firstName;
    private String lastName;
    private Integer age;
    private String events;

    public ParticipantDTO(){ }
    public ParticipantDTO(String firstName, String lastName, Integer age, String events) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.events = events;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getEvents() {
        return events;
    }

    public void setEvents(String events) {
        this.events = events;
    }
}
